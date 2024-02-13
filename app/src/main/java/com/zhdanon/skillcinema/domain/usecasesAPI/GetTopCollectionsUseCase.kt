package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetTopCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(
        topType: String
    ): List<Movie> {
        return repository.getTopCollections(topType, 1)
    }
}