package com.zhdanon.skillcinema.data.api

import com.zhdanon.skillcinema.data.api.model.ResponsePremierMovies
import com.zhdanon.skillcinema.data.api.model.ResponseTopMovies
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
}