package com.zhdanon.skillcinema.domain

import com.zhdanon.skillcinema.domain.models.Movie

interface Repository {
    suspend fun getTopCollections(type: String, page: Int): List<Movie>

    suspend fun getPremierCollection(year: Int, month: String): List<Movie>
}