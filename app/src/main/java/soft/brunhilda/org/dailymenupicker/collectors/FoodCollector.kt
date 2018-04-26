package soft.brunhilda.org.dailymenupicker

import noman.googleplaces.Place

/**
 * Created by mhajas on 4/21/18.
 */
abstract class FoodCollector {

    abstract fun getRestaurantData(googlePlaceData: Place, restaurantProcessor: CollectedRestaurantProcessor)
}