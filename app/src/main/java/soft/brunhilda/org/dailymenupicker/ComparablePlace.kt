package soft.brunhilda.org.dailymenupicker

import noman.googleplaces.Place
import java.io.Serializable

class ComparablePlace(place: Place) : Place(), Serializable{

    override fun equals(other: Any?) : Boolean{
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Place

        return (this.placeId == other.placeId)
    }

    override fun hashCode() : Int {
        return this.placeId.hashCode()
    }

    init {
        icon = place.icon
        location = place.location
        name = place.name
        placeId = place.placeId
        types = place.types
        vicinity = place.vicinity
    }
}