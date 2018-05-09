package soft.brunhilda.org.dailymenupicker.resolvers

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

interface DataResolver {

    val callback: (Map<Place, RestaurantWeekData?>) -> Unit
    val resolvedPlaces: Map<Place, RestaurantWeekData?>
    fun resolvePlaces(places: List<Place>)
}