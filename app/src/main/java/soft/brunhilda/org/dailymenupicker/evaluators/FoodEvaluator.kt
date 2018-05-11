package soft.brunhilda.org.dailymenupicker.evaluators

import soft.brunhilda.org.dailymenupicker.database.FavoriteIngredientEntity
import soft.brunhilda.org.dailymenupicker.database.FavoriteRestaurantEntity
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

class FoodEvaluator : Evaluator<FoodEntityAdapterItem> {

    companion object {
        private var mInstance: FoodEvaluator = FoodEvaluator()

        @Synchronized
        fun getInstance(): FoodEvaluator {
            return mInstance
        }
    }

    override fun evaluate(toEvaluate: MutableList<FoodEntityAdapterItem>,
                          favoriteRestaurants: List<FavoriteRestaurantEntity>,
                          favoriteIngredients: List<FavoriteIngredientEntity>): MutableList<FoodEntityAdapterItem> {

        return toEvaluate.map {foodEntityAdapterItem ->  
            foodEntityAdapterItem.preferenceEvaluation = 0.0

            // evaluate food ingredients
            val containedIngrediets = foodEntityAdapterItem.foodEntity.tags?.intersect(favoriteIngredients.map { it.ingredient })

            if (containedIngrediets != null) {
                // score 0 - 5 based on how many of ingredients contained in food is favorite for user
                foodEntityAdapterItem.preferenceEvaluation += 5.0 * (containedIngrediets.size / foodEntityAdapterItem.foodEntity.tags!!.size) // can't be null because if it is null, the intersection will be null
            }

            // evaluate restaurant in which food is served
            if (favoriteRestaurants.any { favoriteRestaurantEntity -> favoriteRestaurantEntity.placeId == foodEntityAdapterItem.googlePlace.placeId }) {
                foodEntityAdapterItem.preferenceEvaluation += 5 // maybe later we can add points based on some rating of restaurant by user, for now just boolean either it is favorite -> full score or not -> 1
            } else {
                foodEntityAdapterItem.preferenceEvaluation += 1
            }

            return@map foodEntityAdapterItem
        }.toMutableList()
    }
}