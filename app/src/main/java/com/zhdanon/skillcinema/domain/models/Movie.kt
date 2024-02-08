package com.zhdanon.skillcinema.domain.models

data class Movie(
    val movieId: Int,
    val name: String,
    val poster: String,
    val rating: String?,
    val genres: List<Genre>
)