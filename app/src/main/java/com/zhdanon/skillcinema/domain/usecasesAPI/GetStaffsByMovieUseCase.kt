package com.zhdanon.skillcinema.domain.usecasesAPI

import com.zhdanon.skillcinema.domain.Repository
import com.zhdanon.skillcinema.domain.models.Staff
import javax.inject.Inject

class GetStaffsByMovieUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(movieId: Int): List<Staff> {
        return repository.getStaffsByMovie(movieId)
    }
}