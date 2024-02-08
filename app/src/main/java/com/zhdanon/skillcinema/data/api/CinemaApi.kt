package com.zhdanon.skillcinema.data.api

import com.zhdanon.skillcinema.data.api.model.ResponsePremierMovies
import com.zhdanon.skillcinema.data.api.model.ResponseTopMovies
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CinemaApi {
    // FragmentTopCollections
    @GET("v2.2/films/collections")
    suspend fun getFilmsTop(
        @Query("type") type: String,
        @Query("page") page: Int
    ): ResponseTopMovies

    @GET("v2.2/films/premieres")
    suspend fun getPremier(
        @Query("year") year: Int,
        @Query("month") month: String
    ): ResponsePremierMovies

    companion object {
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"
        private const val API_KEY = "ffcd0204-2065-4214-b6ae-aa29f5fe4003"

        private val interceptor = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(API_KEY))
            .build()

        fun getInstance(): CinemaApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(interceptor)
                .build()
                .create(CinemaApi::class.java)
        }
    }
}