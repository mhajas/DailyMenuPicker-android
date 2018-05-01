package soft.brunhilda.org.dailymenupicker

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

/**
 * Created by mhajas on 4/21/18.
 */
interface CollectedRestaurantProcessor {
    fun displayCollectedRestaurant(restaurantDailyData: RestaurantWeekData)

    fun displayNotFoundRestaurant(placeData: Place)
}