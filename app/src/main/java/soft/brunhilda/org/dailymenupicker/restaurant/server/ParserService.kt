package cz.muni.fi.pv239.cv2retrofit

import pv239.fi.muni.cz.dailymenupicker.parser.config.Source
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ParserService {
	@GET("users/{username}")
	fun getParserConfig(@Path("username") username: String): Call<Source>
}
