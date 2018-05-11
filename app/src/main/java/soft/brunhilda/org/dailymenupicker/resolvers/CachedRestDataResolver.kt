package soft.brunhilda.org.dailymenupicker.resolvers

import com.orhanobut.hawk.Hawk
import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.collectors.rest.RetrofitApi
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer

class CachedRestDataResolver(
        override var callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit = {},
        override val resolvedPlaces: MutableMap<ComparablePlace, RestaurantWeekData?> = mutableMapOf())
    : DataResolver {

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
            val restaurantWTD = foodService.getData(googlePlace.placeId).execute().body()

            if (restaurantWTD != null) {
                restaurantWeekData = RestaurantWeekData(googlePlace, restaurantWTD)
                Hawk.put(googlePlace.placeId, restaurantWeekData)
            }
        }

        resolvedPlaces[googlePlace] = restaurantWeekData
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

        callback(resolvedPlaces)
    }
}