package cz.muni.fi.pv239.cv2retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Api {
	private val url = "https://api.github.com"

	private var retrofit: Retrofit = Retrofit.Builder()
			.baseUrl(url)
			.addConverterFactory(GsonConverterFactory.create())
			.build()

	fun get(): ParserService {
		return retrofit.create(ParserService::class.java)
	}
}

