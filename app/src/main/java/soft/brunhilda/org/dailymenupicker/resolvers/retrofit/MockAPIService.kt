package soft.brunhilda.org.dailymenupicker.collectors.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekTransferData

interface MockAPIService {
	@GET("restaurant/{placeID}")
	fun getData(@Path("placeID") placeID: String): Call<RestaurantWeekTransferData>
}
