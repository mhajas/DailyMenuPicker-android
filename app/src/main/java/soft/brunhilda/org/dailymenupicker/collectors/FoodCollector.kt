package soft.brunhilda.org.dailymenupicker

/**
 * Created by mhajas on 4/21/18.
 */
abstract class FoodCollector {

    abstract fun getRestaurantData(googlePlaceID: String, restaurantProcessor: CollectedRestaurantProcessor)
}