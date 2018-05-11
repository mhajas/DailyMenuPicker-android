package soft.brunhilda.org.dailymenupicker.evaluators

import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

class FoodEvaluator : Evaluator<FoodEntityAdapterItem> {

    companion object {
        private var mInstance: FoodEvaluator = FoodEvaluator()

        @Synchronized
        fun getInstance(): FoodEvaluator {
            return mInstance
        }
    }

    override fun evaluate(toEvaluate: MutableList<FoodEntityAdapterItem>): MutableList<FoodEntityAdapterItem> {

        return toEvaluate.map {
            it.preferenceEvaluation = 2 // TODO: evaluate by favorite restaurants & food from database
            return@map it
        }.toMutableList()
    }
}