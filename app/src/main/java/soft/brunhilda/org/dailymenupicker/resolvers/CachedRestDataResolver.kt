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
        override val resolvedPlaces: MutableMap<ComparablePlace, RestaurantWeekData?> = mutableMapOf())
    : DataResolver {

    var callbackCalled = 0

    private val foodService = RetrofitApi().get()
    companion object {

        private var mInstance: CachedRestDataResolver = CachedRestDataResolver()
        @Synchronized
        fun getInstance(): CachedRestDataResolver {
            return mInstance
        }

    }

    private fun resolvePlace(googlePlace: ComparablePlace) {
        var restaurantWeekData: RestaurantWeekData? = Hawk.get(googlePlace.placeId, null)

        if (restaurantWeekData == null) {
            callbackCalled++
            foodService.getData(googlePlace.placeId).enqueue(object : Callback<RestaurantWeekTransferData> {
                override fun onResponse(call: Call<RestaurantWeekTransferData>?, response: Response<RestaurantWeekTransferData>?) {
                    if (response != null && response.isSuccessful) {
                        val restaurantWTD = response.body()
                        if (restaurantWTD != null) {
                            restaurantWeekData = RestaurantWeekData(googlePlace, restaurantWTD)
                            Hawk.put(googlePlace.placeId, restaurantWeekData)
                        }
                    }

                    resolvedPlaces[googlePlace] = restaurantWeekData
                    if (--callbackCalled == 0) {
                        callback(resolvedPlaces)
                    }
                }

                override fun onFailure(call: Call<RestaurantWeekTransferData>?, t: Throwable?) {

                }
            })
        }
    }

    override fun resolvePlaces(places: List<ComparablePlace>, callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit) {
        this.callback = callback

        val oldResolvedPlaces: MutableMap<ComparablePlace, RestaurantWeekData?> = resolvedPlaces.toMutableMap()
        resolvedPlaces.clear()

        places.forEach { place ->
            run {
                if (oldResolvedPlaces[place] != null) {
                    resolvedPlaces[place] = oldResolvedPlaces[place]
                    return@forEach
                }

                resolvePlace(place)
            }
        }

        if (callbackCalled == 0) {
            callback(resolvedPlaces)
        }
    }
}