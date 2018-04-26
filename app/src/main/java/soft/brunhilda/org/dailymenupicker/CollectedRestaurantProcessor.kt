package soft.brunhilda.org.dailymenupicker

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData

/**
 * Created by mhajas on 4/21/18.
 */
interface CollectedRestaurantProcessor {
    fun displayCollectedRestaurant(restaurantDailyData: RestaurantDailyData)

    fun displayNotFoundRestaurant(placeData: Place)
}