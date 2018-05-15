package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place

data class RestaurantEntityAdapterItem (
        val googlePlace: Place,
        val averagePrice: Double,
        val soupPrice: Int?,
        val restaurantWeekData: RestaurantWeekData
) : EvaluatableEntity()