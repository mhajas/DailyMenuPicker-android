package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place

data class RestaurantWeekData(var googlePlaceData: Place, var soupIncludedInPrice: Boolean, var menuForDays: List<RestaurantDailyData?>, var weekNumber: Int) {

    constructor(googlePlaceData: Place, data: RestaurantWeekTransferData)
            :this(googlePlaceData, data.soupIncludedInPrice, data.menuForDays, data.weekNumber)

    fun findTodayMenu(): RestaurantDailyData? {
        if (!isCurrent()) {
            return null
        }

        return menuForDays[0] // TODO: work with dates
    }

    fun isCurrent(): Boolean {
        return true
    }
}