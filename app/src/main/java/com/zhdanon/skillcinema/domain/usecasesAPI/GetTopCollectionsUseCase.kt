package com.zhdanon.skillcinema.domain.usecasesAPI

import androidx.paging.PagingData
import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(topType: String): List<Movie> {
        return repository.getTopCollections(topType)
    }

    fun executePaging(topType: String): Flow<PagingData<Movie>> {
        return repository.getTopCollectionsPaging(type = topType)
    }
}