package soft.brunhilda.org.dailymenupicker.database

import com.google.gson.annotations.SerializedName

enum class Ingredient {

    @SerializedName("ryza")
    RYZA,

    @SerializedName("zemiaky")
    ZEMIAKY,

    @SerializedName("knedla")
    KNEDLA,

    @SerializedName("zelenina")
    ZELENINA,

    @SerializedName("kuracina")
    KURACINA,

    @SerializedName("bravcovina")
    BRAVCOVINA,

    @SerializedName("hovadzina")
    HOVADZINA;

    val value: Int
        get() = ordinal + 1

    companion object {

        private val ENUMS = Ingredient.values()

        fun of(ingredient: Int?): Ingredient {
            if (ingredient == null) {
                throw RuntimeException("ingredient was null")
            }
            return ENUMS[ingredient - 1]
        }

    }
}
