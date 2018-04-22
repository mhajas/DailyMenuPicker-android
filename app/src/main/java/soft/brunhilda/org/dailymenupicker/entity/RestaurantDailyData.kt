package soft.brunhilda.org.dailymenupicker.entity

/**
 * Created by mhajas on 4/21/18.
 */
data class RestaurantDailyData (var restaurantName: String, var soup: List<FoodEntity>, var menu: List<FoodEntity>, var soupIncludedInPrice: Boolean) {}