package soft.brunhilda.org.dailymenupicker

import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData

/**
 * Created by mhajas on 4/21/18.
 */
interface CollectedRestaurantProcessor {
    fun displayCollectedRestaurant(placeID: String, restaurantDailyData: RestaurantDailyData?)
}