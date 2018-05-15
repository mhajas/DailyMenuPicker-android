package soft.brunhilda.org.dailymenupicker.preparers

import android.location.Location
import com.orhanobut.hawk.Hawk
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.ComparablePlace

class NearestPlacesDataPreparer private constructor(
        override var callback: (Set<ComparablePlace>) -> Unit = {},
        override val preparedPlaces: MutableSet<ComparablePlace> = mutableSetOf(),
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

    override fun findPlaces(callback: (Set<ComparablePlace>) -> Unit) {
        this.callback = callback

        val newPosition = getCurrentPosition()

        if (lastPosition == null) {
            lastPosition = Hawk.get("lastPosition")

            val cachedData: Set<ComparablePlace>? = Hawk.get("lastPositionResult", null)

            if (cachedData != null) {
                println("Find cached data in Hawk")
                preparedPlaces.addAll(cachedData)
            }
        }

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
                .radius(1000)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()

        lastPosition = newPosition
        Hawk.put("lastPosition", newPosition)
    }

    private fun getCurrentPosition(): Location {
        val returnVal = Location("")
        returnVal.latitude = 49.2227476
        returnVal.longitude = 16.584627

        return returnVal
    }

    private fun getDistanceBetweenTwoPositions(first: Location, second: Location?): Float {

        if (second == null) {
            return 0.1F
        }

        return first.distanceTo(second)
    }

    override fun onPlacesFailure(e: PlacesException?) {
        state = DataPreparationState.FINISHED

        Hawk.put("lastPositionResult", preparedPlaces)

        callback(preparedPlaces)
    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        if (places != null) {
            places.forEach{ println("Found restaurant '${it.placeId}' name: ${it.name}")}
            preparedPlaces.addAll(places.map { ComparablePlace(it) })
        }
    }

    override fun onPlacesFinished() {
        state = DataPreparationState.FINISHED

        Hawk.put("lastPositionResult", preparedPlaces)

        callback(preparedPlaces)
    }

    override fun onPlacesStart() {

    }
}