package com.zhdanon.skillcinema.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zhdanon.skillcinema.data.api.CinemaApi
import com.zhdanon.skillcinema.data.api.model.mapToDomain
import com.zhdanon.skillcinema.data.api.model.mapToDomainMovie
import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Image
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.MovieDetail
import com.zhdanon.skillcinema.domain.models.MovieGallery
import com.zhdanon.skillcinema.domain.models.Staff
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RepositoryApi @Inject constructor(
    private val api: CinemaApi
) : Repository {
    override suspend fun getTopCollections(type: String): List<Movie> {
        val result = api.getFilmsTop(type, PAGE)
        return result.items.map {
            it.mapToDomainMovie()
        }
    }

    override fun getTopCollectionsPaging(type: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = LOAD_PAGE_SIZE),
            pagingSourceFactory = { TopCollectionPagingSource(api, type) }
        ).flow
    }

    override suspend fun getPremierCollection(year: Int, month: String): List<Movie> {
        return api.getPremier(year, month).items.map { it.mapToDomainMovie() }
    }

    override suspend fun getSeriesByFilter(type: String): List<Movie> {
        return api.getFilmsByFilter(type = type).items.map {
            it.mapToDomainMovie()
        }
    }

    override suspend fun getDetailByMovie(movieId: Int): MovieDetail {
        return api.getMovieById(movieId).mapToDomainMovie()
    }

    override suspend fun getStaffsByMovie(movieId: Int): List<Staff> {
        return api.getStaffsByMovie(movieId).map { it.mapToDomain() }
    }

    override suspend fun getImages(movieId: Int, category: String): MovieGallery {
        return api.getFilmImages(movieId, category).mapToDomain()
    }

    override fun getImagesPaging(movieId: Int, category: StateFlow<String>): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(pageSize = LOAD_PAGE_SIZE),
            pagingSourceFactory = { GalleryPagingSource(api, movieId, category) }
        ).flow
    }

    companion object {
        private const val LOAD_PAGE_SIZE = 20
        private const val PAGE = 1
    }
}