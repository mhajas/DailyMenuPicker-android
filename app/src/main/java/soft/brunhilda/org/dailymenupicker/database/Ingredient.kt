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
    HOVADZINA
}
