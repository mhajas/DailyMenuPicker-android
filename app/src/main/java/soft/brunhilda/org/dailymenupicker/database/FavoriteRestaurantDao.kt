package soft.brunhilda.org.dailymenupicker.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface FavoriteRestaurantDao {

    @Insert
    fun insert(restaurant: FavoriteRestaurantEntity)

    @Delete
    fun delete(restaurant: FavoriteRestaurantEntity)

    @Query("SELECT * FROM favorite_restaurant")
    fun findAll(): List<FavoriteRestaurantEntity>

    @Query("SELECT * FROM favorite_restaurant WHERE placeId = :placeId")
    fun getByPlaceId(placeId: String): FavoriteRestaurantEntity?

}
