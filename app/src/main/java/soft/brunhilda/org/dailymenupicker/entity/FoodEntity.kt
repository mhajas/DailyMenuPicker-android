package soft.brunhilda.org.dailymenupicker.entity

import soft.brunhilda.org.dailymenupicker.database.Ingredient

/**
 * Created by mhajas on 4/21/18.
 */
data class FoodEntity (var name : String, var price : Int?, var tags: List<Ingredient>?) {}