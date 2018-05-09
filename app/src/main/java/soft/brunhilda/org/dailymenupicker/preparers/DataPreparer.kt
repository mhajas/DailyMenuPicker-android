package soft.brunhilda.org.dailymenupicker.preparers

import noman.googleplaces.Place

interface DataPreparer {

    val callback: (List<Place>) -> Unit
    val preparedPlaces: List<Place>
    var state: DataPreparationState
    fun findPlaces()
}

enum class DataPreparationState { IN_PROGRESS, FINISHED }