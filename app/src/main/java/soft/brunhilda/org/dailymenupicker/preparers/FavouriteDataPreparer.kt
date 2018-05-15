package soft.brunhilda.org.dailymenupicker.preparers

import android.location.Location
import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.database.DatabaseManager

class FavouriteDataPreparer(
        override var callback: (Set<ComparablePlace>) -> Unit = {},
        override val preparedPlaces: MutableSet<ComparablePlace> = mutableSetOf(),
        override var state: DataPreparationState = DataPreparationState.IN_PROGRESS,
        private val database: DatabaseManager)
    : DataPreparer{

    override fun findPlaces(callback: (Set<ComparablePlace>) -> Unit) {
        this.callback = callback
        val listOfFavouriteRestaurant = database.getAllFavouritePlaces()
        listOfFavouriteRestaurant.forEach{ restaurant ->
            val place = Place()
            place.name = restaurant.name
            place.placeId = restaurant.placeId
            val location = Location("")
            location.latitude = restaurant.latitude
            location.longitude = restaurant.longitude
            place.location = location
            preparedPlaces.add(ComparablePlace(place))
        }
        callback(preparedPlaces)
        state = DataPreparationState.FINISHED
    }
}