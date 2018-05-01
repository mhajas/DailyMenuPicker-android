package soft.brunhilda.org.dailymenupicker.entity

data class RestaurantWeekTransferData(var soupIncludedInPrice: Boolean, var menuForDays: List<RestaurantDailyData>) {
}