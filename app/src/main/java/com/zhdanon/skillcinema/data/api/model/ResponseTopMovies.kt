package com.zhdanon.skillcinema.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.Genre

@JsonClass(generateAdapter = true)
data class ResponseTopMovies(
    @Json(name = "items") val items: List<MovieTop>,
    @Json(name = "total") val total: Int,
    @Json(name = "totalPages") val totalPages: Int
)

@JsonClass(generateAdapter = true)
data class MovieTop(
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "countries") val countries: List<CountryDto>,
    @Json(name = "genres") val genres: List<GenreDto>,
    @Json(name = "ratingKinopoisk") val ratingKinopoisk: Double?,
    @Json(name = "ratingImbd") val ratingImbd: Double?,
    @Json(name = "year") val year: String,
    @Json(name = "type") val type: String,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String
)

fun MovieTop.mapToDomainMovie(): Movie {
    return Movie(
        movieId = this.kinopoiskId,
        name = this.nameRu ?: this.nameEn ?: this.nameOriginal ?: "",
        poster = this.posterUrlPreview,
        rating =
            if (this.ratingKinopoisk != null) this.ratingKinopoisk.toString()
            else if (this.ratingImbd != null) this.ratingImbd.toString()
            else null,
        genres = this.genres.map { Genre(genre = it.genre) }
    )
}