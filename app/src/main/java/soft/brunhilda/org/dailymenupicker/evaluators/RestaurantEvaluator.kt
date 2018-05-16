package soft.brunhilda.org.dailymenupicker.evaluators

import soft.brunhilda.org.dailymenupicker.database.FavoriteIngredientEntity
import soft.brunhilda.org.dailymenupicker.database.FavoriteRestaurantEntity
import soft.brunhilda.org.dailymenupicker.entity.DayOfWeek
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem

class RestaurantEvaluator : Evaluator<RestaurantEntityAdapterItem> {

    companion object {
        private var mInstance: RestaurantEvaluator = RestaurantEvaluator()

        @Synchronized
        fun getInstance(): RestaurantEvaluator {
            return mInstance
        }
    }

    override fun evaluate(toEvaluate: MutableList<RestaurantEntityAdapterItem>,
                          favoriteRestaurants: List<FavoriteRestaurantEntity>,
                          favoriteIngredients: List<FavoriteIngredientEntity>): MutableList<RestaurantEntityAdapterItem> {

        return toEvaluate.map {restaurantEntityAdapterItem ->
            return@map this.evaluateOneElement(restaurantEntityAdapterItem, favoriteRestaurants, favoriteIngredients)
        }.toMutableList()
    }

    /**
     * Evaluate for particular day
     */
    fun evaluateForDay(toEvaluate: MutableList<RestaurantEntityAdapterItem>,
                       favoriteRestaurants: List<FavoriteRestaurantEntity>,
                       favoriteIngredients: List<FavoriteIngredientEntity>,
                       dayOfWeek: DayOfWeek): MutableList<RestaurantEntityAdapterItem> {
        return toEvaluate.map { restaurantEntityAdapterItem ->
            restaurantEntityAdapterItem.preferenceEvaluation = 0.0
            if (restaurantEntityAdapterItem.restaurantWeekData == null) { //doesn't have week data
                restaurantEntityAdapterItem.preferenceEvaluation = -1.0
                return@map restaurantEntityAdapterItem
            } else if (restaurantEntityAdapterItem.restaurantWeekData.findMenuForDay(dayOfWeek) == null) { //doesn't have daily data
                restaurantEntityAdapterItem.preferenceEvaluation = -1.0
                return@map restaurantEntityAdapterItem
            } else {
                return@map evaluateOneElement(restaurantEntityAdapterItem,favoriteRestaurants,favoriteIngredients)
            }
        }.toMutableList()
    }


    /**
     * Evaluation for one element
     */
    private fun evaluateOneElement(restaurantEntityAdapterItem: RestaurantEntityAdapterItem,
                                   favoriteRestaurants: List<FavoriteRestaurantEntity>,
                                   favoriteIngredients: List<FavoriteIngredientEntity>): RestaurantEntityAdapterItem{
        restaurantEntityAdapterItem.preferenceEvaluation = 0.0

        // evaluate restaurant score
        if (favoriteRestaurants.any { favoriteRestaurantEntity -> favoriteRestaurantEntity.placeId == restaurantEntityAdapterItem.googlePlace.placeId }) {
            restaurantEntityAdapterItem.preferenceEvaluation += 5 // maybe later we can add points based on some rating of restaurant by user, for now just boolean either it is favorite -> full score or not -> 1
        } else {
            restaurantEntityAdapterItem.preferenceEvaluation += 1
        }

        val todayData = restaurantEntityAdapterItem.restaurantWeekData?.findTodayMenu()

        // evaluate all food served today and get average of it as final score
        if(todayData != null){
            val foodEvaluations = mutableListOf<Double>()
            todayData.menu.forEach {
                val containedIngrediets = it.tags?.intersect(favoriteIngredients.map { it.ingredient })

                if (containedIngrediets != null && it.tags.isNotEmpty()) {
                    // score 0 - 5 based on how many of ingredients contained in food is favorite for user
                    foodEvaluations.add(5.0 * (containedIngrediets.size.toDouble() / it.tags.size)) // can't be null because if it is null, the intersection will be null
                }
            }
            restaurantEntityAdapterItem.preferenceEvaluation += foodEvaluations.average()
        }
        return restaurantEntityAdapterItem
    }
}