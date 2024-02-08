package com.zhdanon.skillcinema.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "genre") val genre: String
)

@JsonClass(generateAdapter = true)
data class CountryDto(
    @Json(name = "country") val country: String
)