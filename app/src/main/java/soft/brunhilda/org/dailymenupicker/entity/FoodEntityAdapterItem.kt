package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place

/**
 * Created by mhajas on 4/21/18.
 */
data class FoodEntityAdapterItem (var foodEntity: FoodEntity, var restaurantDailyData: RestaurantDailyData, var googlePlace: Place, var preferenceEvaluation: Int = 2) {}