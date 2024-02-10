package com.zhdanon.skillcinema.domain

import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetPremierCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun executePremierCollections(
        year: Int, month: String
    ): List<Movie> {
        return repository.getPremierCollection(year, month)
    }
}