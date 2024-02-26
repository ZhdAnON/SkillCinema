package com.zhdanon.skillcinema.data.api

import com.zhdanon.skillcinema.data.api.model.ResponseDetailMovie
import com.zhdanon.skillcinema.data.api.model.ResponseImages
import com.zhdanon.skillcinema.data.api.model.ResponseMoviesByFilter
import com.zhdanon.skillcinema.data.api.model.ResponsePremierMovies
import com.zhdanon.skillcinema.data.api.model.ResponseSimilarMovies
import com.zhdanon.skillcinema.data.api.model.ResponseStaffsByMovie
import com.zhdanon.skillcinema.data.api.model.ResponseTopMovies
import retrofit2.http.GET
import retrofit2.http.Path
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

    // FragmentTopCollections (TV_SERIES)
    @GET("v2.2/films")
    suspend fun getFilmsByFilter(
        @Query("type") type: String = ""
    ): ResponseMoviesByFilter

    // ----------------------------------

    // FragmentDetailMovie
    @GET("v2.2/films/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): ResponseDetailMovie

    @GET("v1/staff")
    suspend fun getStaffsByMovie(
        @Query("filmId") movieId: Int
    ): List<ResponseStaffsByMovie>

    @GET("v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") id: Int,
        @Query("type") type: String = "",
        @Query("page") page: Int = 1
    ): ResponseImages

    @GET("v2.2/films/{id}/similars")
    suspend fun getSimilarFilms(
        @Path("id") id: Int
    ): ResponseSimilarMovies
}