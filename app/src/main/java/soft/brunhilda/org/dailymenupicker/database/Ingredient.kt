package soft.brunhilda.org.dailymenupicker.database

import com.google.gson.annotations.SerializedName

enum class Ingredient {

    @SerializedName("ryza")
    RICE,

    @SerializedName("zemiaky")
    POTATO,

    @SerializedName("knedla")
    DUMPLING,

    @SerializedName("zelenina")
    VEGETABLE,

    @SerializedName("kuracina")
    CHICKEN,

    @SerializedName("bravcovina")
    PORK,

    @SerializedName("hovadzina")
    BEEF,

    @SerializedName("ryba")
    FISH,

    @SerializedName("cestovina")
    PASTA,

    @SerializedName("sladke")
    SWEET;

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
