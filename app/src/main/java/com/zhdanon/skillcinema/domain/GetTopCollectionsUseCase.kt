package com.zhdanon.skillcinema.domain

import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetTopCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun executeTopCollections(
        topType: String
    ): List<Movie> {
        return repository.getTopCollections(topType, 1)
    }
}