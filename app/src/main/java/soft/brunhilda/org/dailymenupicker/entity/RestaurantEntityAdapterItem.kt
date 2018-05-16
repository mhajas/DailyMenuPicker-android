package soft.brunhilda.org.dailymenupicker.entity

import soft.brunhilda.org.dailymenupicker.ComparablePlace

data class RestaurantEntityAdapterItem (
        val googlePlace: ComparablePlace,
        val restaurantWeekData: RestaurantWeekData?
) : EvaluatableEntity()