package soft.brunhilda.org.dailymenupicker.evaluators

import soft.brunhilda.org.dailymenupicker.database.FavoriteIngredientEntity
import soft.brunhilda.org.dailymenupicker.database.FavoriteRestaurantEntity
import soft.brunhilda.org.dailymenupicker.entity.EvaluatableEntity

interface Evaluator<E> where E : EvaluatableEntity {
    fun evaluate(toEvaluate: MutableList<E>,
                 favoriteRestaurants: List<FavoriteRestaurantEntity>,
                 favoriteIngredients: List<FavoriteIngredientEntity>) : MutableList<E>
}