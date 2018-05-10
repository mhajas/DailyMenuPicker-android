package soft.brunhilda.org.dailymenupicker.preparers

import android.location.Location
import noman.googleplaces.*

class NearestPlacesDataPreparer private constructor(
        override var callback: (List<Place>) -> Unit = {},
        override val preparedPlaces: MutableList<Place> = mutableListOf(),
        override var state: DataPreparationState = DataPreparationState.IN_PROGRESS)
: DataPreparer, PlacesListener {

    var lastPosition: Location? = null

    companion object {
        private var mInstance: NearestPlacesDataPreparer = NearestPlacesDataPreparer()

        @Synchronized
        fun getInstance(): NearestPlacesDataPreparer {
            return mInstance
        }
    }

    override fun findPlaces() {
        val newPosition = getCurrentPosition()

        if (lastPosition != null) {

            // There is already some search on memory, lets find out whether it is actual
            if (getDistanceBetweenTwoPositions(newPosition, lastPosition) < 0.1) {

                // The places in memory is within 100m distance so lets just use those
                callback(preparedPlaces)
                return
            } else {

                // In memory data is from different location lets start searching again
                preparedPlaces.clear()
                state = DataPreparationState.IN_PROGRESS
            }
        }

        NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyAMJQuIQAzLRHdCGbxhfsvr-q7lFEaPxPg")
                .latlng(newPosition.latitude, newPosition.longitude)
                .radius(20)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()

        lastPosition = newPosition
    }

    fun getCurrentPosition(): Location {
        val returnVal = Location("")
        returnVal.latitude = 49.2227476
        returnVal.longitude = 16.584627

        return returnVal
    }

    fun getDistanceBetweenTwoPositions(first: Location, second: Location?): Float {

        if (second == null) {
            return 0.1F
        }

        return first.distanceTo(second)
    }

    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        if (places != null) {
            preparedPlaces.addAll(places)
        }
    }

    override fun onPlacesFinished() {
        state = DataPreparationState.FINISHED

        callback(preparedPlaces)
    }

    override fun onPlacesStart() {

    }
}