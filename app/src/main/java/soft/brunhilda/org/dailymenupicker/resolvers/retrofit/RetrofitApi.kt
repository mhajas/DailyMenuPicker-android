package soft.brunhilda.org.dailymenupicker.resolvers.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitApi {
    private val url = "http://demo6784082.mockable.io/"

    val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

    private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    fun get(): MockAPIService {
        return retrofit.create(MockAPIService::class.java)
    }
}