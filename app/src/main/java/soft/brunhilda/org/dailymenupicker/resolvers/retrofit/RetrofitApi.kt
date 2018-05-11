package soft.brunhilda.org.dailymenupicker.resolvers.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {
	private val url = "http://demo6784082.mockable.io/"

	private var retrofit: Retrofit = Retrofit.Builder()
			.baseUrl(url)
			.addConverterFactory(GsonConverterFactory.create())
			.build()

	fun get(): MockAPIService {
		return retrofit.create(MockAPIService::class.java)
	}
}