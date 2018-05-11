package soft.brunhilda.org.dailymenupicker.resolvers

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

interface DataResolver {

    val callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit
    val resolvedPlaces: Map<ComparablePlace, RestaurantWeekData?>
    fun resolvePlaces(places: List<ComparablePlace>, callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit)
}