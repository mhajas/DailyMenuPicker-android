package soft.brunhilda.org.dailymenupicker.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface FavoriteIngredientDao {

    @Insert
    fun insert(restaurant: FavoriteIngredientEntity)

    @Delete
    fun delete(restaurant: FavoriteIngredientEntity)

    @Query("SELECT * FROM favorite_ingredients")
    fun findAll(): List<FavoriteIngredientEntity>
}
