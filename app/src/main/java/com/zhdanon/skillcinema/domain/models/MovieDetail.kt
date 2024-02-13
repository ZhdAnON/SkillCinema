package com.zhdanon.skillcinema.domain.models

data class MovieDetail(
    val movieId: Int,
    val name: String,
    val poster: String,
    val rating: String?,
    val year: Int?,
    val length: Int?,
    val shortDescription: String?,
    val description: String?,
    val type: String,
    val ageLimit: String?,
    val startYear: Int?,
    val endYear: Int?,
    val serial: Boolean?,
    val genres: List<Genre>,
    val countries: List<Country>
)