package soft.brunhilda.org.dailymenupicker.entity

data class RestaurantWeekTransferData(var soupIncludedInPrice: Boolean, var weekNumber: Int, var menuForDays: List<RestaurantDailyData?>) {
}