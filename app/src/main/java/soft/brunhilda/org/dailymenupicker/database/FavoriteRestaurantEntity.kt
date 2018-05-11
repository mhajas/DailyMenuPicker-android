package soft.brunhilda.org.dailymenupicker.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "favorite_restaurant")
data class FavoriteRestaurantEntity (
        @PrimaryKey
        @ColumnInfo(name = "placeId")
        var placeId: String = "",

        @ColumnInfo(name = "name")
        var name: String = "") {

    override fun toString(): String {
        return "FavoriteRestaurantEntity(placeId='$placeId', name='$name')"
    }
}