package soft.brunhilda.org.dailymenupicker.evaluators

import soft.brunhilda.org.dailymenupicker.entity.EvaluatableEntity

interface Evaluator<E> where E : EvaluatableEntity {
    fun evaluate(toEvaluate: MutableList<E>) : MutableList<E>
}