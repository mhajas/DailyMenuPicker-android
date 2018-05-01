package soft.brunhilda.org.dailymenupicker.collectors.rest

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.FoodCollector
import soft.brunhilda.org.dailymenupicker.CollectedRestaurantProcessor
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

/**
 * Created by mhajas on 4/21/18.
 */
class RESTFoodCollector: FoodCollector() {

    val foodService = RetrofitApi().get()

    override fun getRestaurantData(googlePlaceData: Place, restaurantProcessor: CollectedRestaurantProcessor) {
        val data = foodService.getData(googlePlaceData.placeId).execute().body()

        if (data == null) {
            restaurantProcessor.displayNotFoundRestaurant(googlePlaceData)
        } else {
            restaurantProcessor.displayCollectedRestaurant(RestaurantWeekData(googlePlaceData, data))
        }
    }
}