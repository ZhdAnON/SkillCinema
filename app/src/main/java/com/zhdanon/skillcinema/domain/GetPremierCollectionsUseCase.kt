package com.zhdanon.skillcinema.domain

import com.zhdanon.skillcinema.data.RepositoryApi
import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetPremierCollectionsUseCase @Inject constructor(
    private val repositoryApi: RepositoryApi
) {
    suspend fun executePremierCollections(
        year: Int, month: String
    ): List<Movie> {
        return repositoryApi.getPremierCollection(year, month)
    }
}