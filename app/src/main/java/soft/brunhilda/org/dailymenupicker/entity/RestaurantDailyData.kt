package soft.brunhilda.org.dailymenupicker.entity

import java.util.*

/**
 * Created by mhajas on 4/21/18.
 */
data class RestaurantDailyData (var menu: List<FoodEntity>, var soup: List<FoodEntity>, var date: Date) {}