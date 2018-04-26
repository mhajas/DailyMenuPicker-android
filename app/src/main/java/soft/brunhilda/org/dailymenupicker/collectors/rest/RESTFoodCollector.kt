package soft.brunhilda.org.dailymenupicker.collectors.rest

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.FoodCollector
import soft.brunhilda.org.dailymenupicker.CollectedRestaurantProcessor
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData

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
            restaurantProcessor.displayCollectedRestaurant(RestaurantDailyData(googlePlaceData, data))
        }
    }
}