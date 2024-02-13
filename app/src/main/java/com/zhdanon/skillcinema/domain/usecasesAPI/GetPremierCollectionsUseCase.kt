package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Movie
import javax.inject.Inject

class GetPremierCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(
        year: Int, month: String
    ): List<Movie> {
        return repository.getPremierCollection(year, month)
    }
}