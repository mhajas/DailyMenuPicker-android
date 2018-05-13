package soft.brunhilda.org.dailymenupicker.preparers

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase

class FavouriteDataPreparer(
        override var callback: (Set<ComparablePlace>) -> Unit = {},
        override val preparedPlaces: MutableSet<ComparablePlace> = mutableSetOf(),
        override var state: DataPreparationState = DataPreparationState.IN_PROGRESS,
        private val database: DailyMenuPickerDatabase)
    : DataPreparer{

    override fun findPlaces(callback: (Set<ComparablePlace>) -> Unit) {
        this.callback = callback
        val listOfFavouriteRestaurant = database.favoriteRestaurantDao().findAll()
        listOfFavouriteRestaurant.forEach{ restaurant ->
            val place: Place = Place() //TODO hack  ?
            place.name = restaurant.name
            place.placeId = restaurant.placeId
            preparedPlaces.add(ComparablePlace(place))
        }
        callback(preparedPlaces)
        state = DataPreparationState.FINISHED
    }
}