package com.zhdanon.skillcinema.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.Genre

@JsonClass(generateAdapter = true)
data class ResponsePremierMovies(
    @Json(name = "items") val items: List<MoviePremier>,
    @Json(name = "total") val total: Int
)

@JsonClass(generateAdapter = true)
data class MoviePremier(
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "countries") val countries: List<CountryDto>,
    @Json(name = "genres") val genres: List<GenreDto>,
    @Json(name = "year") val year: Int,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "premiereRu") val premiereRu: String?,
    @Json(name = "duration") val duration: Int?
)

fun  MoviePremier.mapToDomainMovie(): Movie {
    return Movie(
        movieId = this.kinopoiskId,
        name = this.nameRu ?: this.nameEn ?: "",
        poster = this.posterUrlPreview,
        rating = null,
        genres = this.genres.map { Genre(genre = it.genre) }
    )
}