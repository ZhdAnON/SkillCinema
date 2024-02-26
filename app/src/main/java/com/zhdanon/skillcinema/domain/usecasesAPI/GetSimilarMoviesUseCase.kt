package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.MovieSimilar
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(movieId: Int): MovieSimilar {
        return repository.getSimilar(movieId)
    }
}