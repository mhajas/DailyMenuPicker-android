package soft.brunhilda.org.dailymenupicker.resolvers

import com.orhanobut.hawk.Hawk
import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.collectors.rest.RetrofitApi
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer

class CachedRestDataResolver(
        override var callback: (Map<Place, RestaurantWeekData?>) -> Unit = {},
        override val resolvedPlaces: MutableMap<Place, RestaurantWeekData?> = mutableMapOf())
    : DataResolver {

    companion object {
        private var mInstance: CachedRestDataResolver = CachedRestDataResolver()

        @Synchronized
        fun getInstance(): CachedRestDataResolver {
            return mInstance
        }
    }

    val foodService = RetrofitApi().get()

    private fun resolvePlace(googlePlace: Place) {
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

    override fun resolvePlaces(places: List<Place>) {

        val newResolvedPlaces: MutableMap<Place, RestaurantWeekData?> = mutableMapOf()

        places.forEach{
            if (resolvedPlaces[it] != null) {

            }
            resolvePlace(it)
        }

        callback(resolvedPlaces)
    }
}