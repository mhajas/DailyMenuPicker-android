package soft.brunhilda.org.dailymenupicker.preparers

import noman.googleplaces.*

class NearestPlacesDataPreparer(override val callback: (List<Place>) -> Unit, override val preparedPlaces: MutableList<Place> = mutableListOf(), override var state: DataPreparationState = DataPreparationState.IN_PROGRESS)

    : DataPreparer, PlacesListener
{
    override fun findPlaces() {
        if (state == DataPreparationState.FINISHED) {
            callback(preparedPlaces)
        }

        NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyAMJQuIQAzLRHdCGbxhfsvr-q7lFEaPxPg")
                .latlng(49.2227476, 16.584627)
                .radius(20)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()
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