package soft.brunhilda.org.dailymenupicker.resolvers

import com.orhanobut.hawk.Hawk
import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.collectors.rest.RetrofitApi
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

class CachedRestDataResolver(override val callback: (Map<Place, RestaurantWeekData?>) -> Unit, override val resolvedPlaces: MutableMap<Place, RestaurantWeekData?> = mutableMapOf())

    : DataResolver {

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
        places.forEach{
            resolvePlace(it)
        }

        callback(resolvedPlaces)
    }
}