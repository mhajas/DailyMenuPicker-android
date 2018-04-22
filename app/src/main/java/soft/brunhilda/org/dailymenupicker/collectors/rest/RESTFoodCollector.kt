package soft.brunhilda.org.dailymenupicker.collectors.rest

import soft.brunhilda.org.dailymenupicker.FoodCollector
import soft.brunhilda.org.dailymenupicker.CollectedRestaurantProcessor

/**
 * Created by mhajas on 4/21/18.
 */
class RESTFoodCollector: FoodCollector() {

    val foodService = RetrofitApi().get()

    override fun getRestaurantData(googlePlaceID: String, restaurantProcessor: CollectedRestaurantProcessor) {
        restaurantProcessor.displayCollectedRestaurant(googlePlaceID, foodService.getData(googlePlaceID).execute().body())
    }
}