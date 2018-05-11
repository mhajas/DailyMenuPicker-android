package soft.brunhilda.org.dailymenupicker.preparers

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.ComparablePlace

interface DataPreparer {

    val callback: (List<ComparablePlace>) -> Unit
    val preparedPlaces: List<ComparablePlace>
    var state: DataPreparationState
    fun findPlaces(callback: (List<ComparablePlace>) -> Unit)
}

enum class DataPreparationState { IN_PROGRESS, FINISHED }