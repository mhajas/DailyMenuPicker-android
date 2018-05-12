package soft.brunhilda.org.dailymenupicker.preparers

import soft.brunhilda.org.dailymenupicker.ComparablePlace

interface DataPreparer {

    val callback: (Set<ComparablePlace>) -> Unit
    val preparedPlaces: Set<ComparablePlace>
    var state: DataPreparationState
    fun findPlaces(callback: (Set<ComparablePlace>) -> Unit)
}

enum class DataPreparationState { IN_PROGRESS, FINISHED }