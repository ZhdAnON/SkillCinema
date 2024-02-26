package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.app.core.CategoriesMovies
import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetMoviesByFilterUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(
        type: String = CategoriesMovies.TV_SERIES.name
    ): List<Movie> {
        return repository.getSeriesByFilter(type)
    }
}