package com.zhdanon.skillcinema.domain.usecasesAPI

import androidx.paging.PagingData
import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Image
import com.zhdanon.skillcinema.domain.models.MovieGallery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetImagesByMovieUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(movieId: Int, category: String): MovieGallery {
        return repository.getImages(movieId, category)
    }

    fun executePaging(movieId: Int, category: StateFlow<String>): Flow<PagingData<Image>> {
        return repository.getImagesPaging(movieId, category)
    }
}