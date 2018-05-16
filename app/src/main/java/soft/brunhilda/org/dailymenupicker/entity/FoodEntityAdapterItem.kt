package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.ComparablePlace

/**
 * Created by mhajas on 4/21/18.
 */
data class FoodEntityAdapterItem (
        var foodEntity: FoodEntity,
        var restaurantDailyData: RestaurantDailyData,
        var googlePlace: ComparablePlace,
        var dayOfWeek: DayOfWeek
) : EvaluatableEntity()