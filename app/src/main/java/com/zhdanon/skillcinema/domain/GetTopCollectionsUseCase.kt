package com.zhdanon.skillcinema.domain

import com.zhdanon.skillcinema.data.RepositoryApi
import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetTopCollectionsUseCase @Inject constructor(
    private val repositoryApi: RepositoryApi
) {
    suspend fun executeTopCollections(
        topType: String
    ): List<Movie> {
        return repositoryApi.getTopCollections(topType, 1)
    }
}