package com.zhdanon.skillcinema.domain

import androidx.paging.PagingData
import com.zhdanon.skillcinema.app.core.CategoriesMovies
import com.zhdanon.skillcinema.domain.models.Image
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.MovieDetail
import com.zhdanon.skillcinema.domain.models.MovieGallery
import com.zhdanon.skillcinema.domain.models.Staff
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    suspend fun getTopCollections(type: String): List<Movie>

    fun getTopCollectionsPaging(type: String): Flow<PagingData<Movie>>

    suspend fun getPremierCollection(year: Int, month: String): List<Movie>

    suspend fun getSeriesByFilter(
        type: String = CategoriesMovies.TV_SERIES.name
    ): List<Movie>

    suspend fun getDetailByMovie(movieId: Int): MovieDetail

    suspend fun getStaffsByMovie(movieId: Int): List<Staff>

    suspend fun getImages(movieId: Int, category: String): MovieGallery

    fun getImagesPaging(movieId: Int, category: StateFlow<String>): Flow<PagingData<Image>>
}