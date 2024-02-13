package com.zhdanon.skillcinema.domain

import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.MovieDetail

interface Repository {
    suspend fun getTopCollections(type: String, page: Int): List<Movie>

    suspend fun getPremierCollection(year: Int, month: String): List<Movie>

    suspend fun getDetailByMovie(movieId: Int): MovieDetail

    suspend fun getSeriesByFilter(
        type: String = CategoriesMovies.TV_SERIES.name
    ): List<Movie>
}