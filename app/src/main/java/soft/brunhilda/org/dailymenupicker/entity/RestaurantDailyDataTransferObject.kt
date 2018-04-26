package soft.brunhilda.org.dailymenupicker.entity

data class RestaurantDailyDataTransferObject (var restaurantName: String, var soup: List<FoodEntity>, var menu: List<FoodEntity>, var soupIncludedInPrice: Boolean) {}