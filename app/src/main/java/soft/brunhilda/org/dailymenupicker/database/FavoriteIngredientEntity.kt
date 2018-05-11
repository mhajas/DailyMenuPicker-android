package soft.brunhilda.org.dailymenupicker.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter


@Entity(tableName = "favorite_ingredients")
data class FavoriteIngredientEntity(
        @PrimaryKey
        @ColumnInfo(name = "ingredient")
        var ingredient: Ingredient)
{

        override fun toString(): String {
                return "FavoriteIngredientEntity(ingredient=$ingredient)"
        }
}

class IngredientEnumConverter {
        companion object {

                @TypeConverter
                @JvmStatic
                fun daysOfWeekToString(ingredient: Ingredient): String? = ingredient.value.toString()

                @TypeConverter
                @JvmStatic
                fun stringToDaysOfWeek(ingredient: String?): Ingredient? = Ingredient.of(ingredient?.toInt())
        }
}