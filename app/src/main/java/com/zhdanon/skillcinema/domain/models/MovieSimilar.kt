package com.zhdanon.skillcinema.domain.models

data class MovieSimilar(
    val total: Int,
    val movies: List<Movie>
)