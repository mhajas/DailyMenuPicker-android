package soft.brunhilda.org.dailymenupicker.database

import android.arch.persistence.room.Room
import android.content.Context
import soft.brunhilda.org.dailymenupicker.ComparablePlace

class DatabaseManager(
        val context: Context
){
    val database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
            .allowMainThreadQueries()
            .build()

    fun addFavouritePlace(place: ComparablePlace){
        val restaurantEntity = FavoriteRestaurantEntity()
        restaurantEntity.name = place.name
        restaurantEntity.placeId = place.placeId
        restaurantEntity.latitude = place.latitude
        restaurantEntity.longitude = place.longitude
        database.favoriteRestaurantDao().insert(restaurantEntity)
    }

    fun deleteFavouritePlace(place: ComparablePlace){
        val favourite = database.favoriteRestaurantDao().getByPlaceId(place.placeId)
        if (favourite != null) {
            database.favoriteRestaurantDao().delete(favourite)
        }
    }

    fun getFavouritePlace(id: String): FavoriteRestaurantEntity? {
        return database.favoriteRestaurantDao().getByPlaceId(id)
    }

    fun isPlaceInDb(id: String): Boolean{
        return getFavouritePlace(id) != null
    }

    fun getAllFavouritePlaces(): List<FavoriteRestaurantEntity>{
        return database.favoriteRestaurantDao().findAll()
    }

    fun addFavouriteIngredient(favoriteIngredient: FavoriteIngredientEntity){
        database.favoriteIngredientDao().insert(favoriteIngredient)
    }

    fun deleteFavouriteIngredient(favoriteIngredient: FavoriteIngredientEntity){
        database.favoriteIngredientDao().delete(favoriteIngredient)
    }

    fun getAllFavouriteIngredients(): List<FavoriteIngredientEntity>{
        return database.favoriteIngredientDao().findAll()
    }

}