package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place

data class RestaurantEntityAdapterItem (
        val googleData: Place,
        val averagePrice: Double,
        val soupPrice: Int?
) : EvaluatableEntity() {}