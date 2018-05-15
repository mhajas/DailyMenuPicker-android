package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place
import java.util.*

data class RestaurantWeekData(var googlePlaceData: Place, var soupIncludedInPrice: Boolean, var menuForDays: List<RestaurantDailyData?>, var weekNumber: Int) {

    constructor(googlePlaceData: Place, data: RestaurantWeekTransferData)
            : this(googlePlaceData, data.soupIncludedInPrice, data.menuForDays, data.weekNumber)

    fun findTodayMenu(): RestaurantDailyData? {
        if (!isCurrent()) {
            return null
        }

        return findMenuForDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) // Calendar indexing from 1 and starting from Sunday so Tuesday == 3
    }

    fun findMenuForDay(day: DayOfWeek): RestaurantDailyData? {
        return findMenuForDay(day.ordinal)
    }

    fun findMenuForDay(dayNumber: Int): RestaurantDailyData? {
        if (!isCurrent()) {
            return null
        }

        return menuForDays.getOrNull(dayNumber)
    }

    fun isCurrent(): Boolean {
        return weekNumber == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
    }
}

enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
}