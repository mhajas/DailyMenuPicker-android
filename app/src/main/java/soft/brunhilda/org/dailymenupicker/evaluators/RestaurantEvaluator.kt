package soft.brunhilda.org.dailymenupicker.evaluators

import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem

class RestaurantEvaluator : Evaluator<RestaurantEntityAdapterItem>  {

    companion object {
        private var mInstance: RestaurantEvaluator = RestaurantEvaluator()

        @Synchronized
        fun getInstance(): RestaurantEvaluator {
            return mInstance
        }
    }

    override fun evaluate(toEvaluate: MutableList<RestaurantEntityAdapterItem>): MutableList<RestaurantEntityAdapterItem> {

        return toEvaluate.map {
            it.preferenceEvaluation = 2 // TODO: evaluate by favorite restaurants from database
            return@map it
        }.toMutableList()
    }

}