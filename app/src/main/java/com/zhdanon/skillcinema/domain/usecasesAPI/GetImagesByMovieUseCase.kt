package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.MovieGallery
import javax.inject.Inject

class GetImagesByMovieUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(movieId: Int): MovieGallery {
        return repository.getImages(movieId)
    }
}