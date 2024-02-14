package com.zhdanon.skillcinema.data

import com.zhdanon.skillcinema.data.api.CinemaApi
import com.zhdanon.skillcinema.data.api.model.mapToDomain
import com.zhdanon.skillcinema.data.api.model.mapToDomainMovie
import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.MovieDetail
import com.zhdanon.skillcinema.domain.models.Staff
import javax.inject.Inject

class RepositoryApi @Inject constructor(
    private val api: CinemaApi
) : Repository {
    override suspend fun getTopCollections(type: String, page: Int): List<Movie> {
        val result = api.getFilmsTop(type, page)
        return result.items.map {
            it.mapToDomainMovie()
        }
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
}