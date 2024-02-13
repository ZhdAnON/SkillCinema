package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.MovieDetail
import javax.inject.Inject

class GetDetailMovieUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(movieId: Int): MovieDetail {
        return repository.getDetailByMovie(movieId)
    }
}