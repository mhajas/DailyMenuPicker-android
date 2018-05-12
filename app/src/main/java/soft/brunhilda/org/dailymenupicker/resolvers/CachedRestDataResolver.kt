package soft.brunhilda.org.dailymenupicker.resolvers

import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekTransferData
import soft.brunhilda.org.dailymenupicker.resolvers.retrofit.RetrofitApi


class CachedRestDataResolver(
        override var callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit = {},
        override val storage: ResolvedDataStorage = ResolvedDataStorage.getInstance())
    : DataResolver {

    var callbackCalled: Int = 0
    val LOCK: Object = Object()

    private val foodService = RetrofitApi().get()

    private fun resolvePlace(googlePlace: ComparablePlace) {
        var restaurantWeekData: RestaurantWeekData? = Hawk.get(googlePlace.placeId, null)

        if (!Hawk.contains(googlePlace.placeId) && !isInCacheAsUnknown(googlePlace)) {
            synchronized(LOCK) {
                callbackCalled++
                println("Calling call number: $callbackCalled")
            }

            foodService.getData(googlePlace.placeId).enqueue(object : Callback<RestaurantWeekTransferData> {
                override fun onResponse(call: Call<RestaurantWeekTransferData>?, response: Response<RestaurantWeekTransferData>?) {
                    if (response != null && response.isSuccessful) {
                        val restaurantWTD = response.body()
                        if (restaurantWTD != null) {
                            restaurantWeekData = RestaurantWeekData(googlePlace, restaurantWTD)
                            Hawk.put(googlePlace.placeId, restaurantWeekData)
                        }
                    }

                    synchronized(LOCK) {
                        if (restaurantWeekData == null) {
                            saveNotKnownRestaurant(googlePlace)
                        }

                        storage.resolvedPlaces[googlePlace] = restaurantWeekData
                        --callbackCalled
                        println("Finishing call number: $callbackCalled")
                        if (callbackCalled <= 0) {
                            callback(storage.resolvedPlaces)
                        }
                    }
                }

                override fun onFailure(call: Call<RestaurantWeekTransferData>?, t: Throwable?) {
                    synchronized(LOCK) {
                        --callbackCalled
                        println("Finishing failed call number: $callbackCalled")

                        saveNotKnownRestaurant(googlePlace)

                        if (callbackCalled <= 0) {
                            callback(storage.resolvedPlaces)
                        }
                    }
                }
            })
        } else {
            println("Data from Hawk: $restaurantWeekData")
            storage.resolvedPlaces[googlePlace] = restaurantWeekData
        }
    }

    private fun saveNotKnownRestaurant(place: ComparablePlace) {
        val set = Hawk.get<MutableSet<String>>("notKnownRestaurants", mutableSetOf())

        set.add(place.placeId)

        Hawk.put("notKnownRestaurants", set)
    }

    private fun isInCacheAsUnknown(place: ComparablePlace): Boolean {
        val set = Hawk.get<MutableSet<String>>("notKnownRestaurants", mutableSetOf())
        return set.contains(place.placeId)
    }

    override fun resolvePlaces(places: List<ComparablePlace>, callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit) {
        this.callback = callback

        val oldResolvedPlaces: MutableMap<ComparablePlace, RestaurantWeekData?> = storage.resolvedPlaces.toMutableMap()
        storage.resolvedPlaces.clear()

        places.forEach { place ->
            run {
                if (oldResolvedPlaces[place] != null) {
                    println("Data from memory: ${oldResolvedPlaces[place]}")
                    storage.resolvedPlaces[place] = oldResolvedPlaces[place]
                    return@forEach
                }

                resolvePlace(place)
            }
        }

        synchronized(LOCK) {
            if (callbackCalled == 0) {
                callback(storage.resolvedPlaces)
            }
        }
    }
}

