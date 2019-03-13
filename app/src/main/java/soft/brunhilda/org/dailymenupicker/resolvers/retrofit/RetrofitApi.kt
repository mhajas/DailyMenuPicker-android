package soft.brunhilda.org.dailymenupicker.resolvers.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitApi {
    private val url = "http://dailymenupicker-dailymenupicker.1d35.starter-us-east-1.openshiftapps.com/"

    val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

    private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    fun get(): APIService {
        return retrofit.create(APIService::class.java)
    }
}