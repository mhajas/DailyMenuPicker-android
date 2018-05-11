package soft.brunhilda.org.dailymenupicker.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = [(FavoriteRestaurantEntity::class), (FavoriteIngredientEntity::class)], version = 1)
@TypeConverters(IngredientEnumConverter::class)
abstract class DailyMenuPickerDatabase : RoomDatabase() {
    abstract fun favoriteRestaurantDao(): FavoriteRestaurantDao
    abstract fun favoriteIngredientDao(): FavoriteIngredientDao
}
