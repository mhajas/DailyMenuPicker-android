package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place

data class RestaurantEntityAdapterItem (
        val googlePlace: Place,
        val restaurantWeekData: RestaurantWeekData?
) : EvaluatableEntity()