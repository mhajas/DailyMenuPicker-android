package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place
import java.util.*

data class RestaurantWeekData(var googlePlaceData: Place, var soupIncludedInPrice: Boolean, var menuForDays: List<RestaurantDailyData>) {

    constructor(googlePlaceData: Place, data: RestaurantWeekTransferData)
            :this(googlePlaceData, data.soupIncludedInPrice, data.menuForDays)

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