package com.zhdanon.skillcinema.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.skillcinema.domain.models.Country
import com.zhdanon.skillcinema.domain.models.Genre
import com.zhdanon.skillcinema.domain.models.MovieDetail

@JsonClass(generateAdapter = true)
data class ResponseDetailMovie(
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "kinopoiskHDId") val kinopoiskHDId: String?,
    @Json(name = "imdbId") val imdbId: String?,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "coverUrl") val coverUrl: String?,
    @Json(name = "logoUrl") val logoUrl: String?,
    @Json(name = "reviewsCount") val reviewsCount: Int,
    @Json(name = "ratingGoodReview") val ratingGoodReview: Double?,
    @Json(name = "ratingGoodReviewVoteCount") val ratingGoodReviewVoteCount: Int?,
    @Json(name = "ratingKinopoisk") val ratingKinopoisk: Double?,
    @Json(name = "ratingKinopoiskVoteCount") val ratingKinopoiskVoteCount: Int?,
    @Json(name = "ratingImdb") val ratingImdb: Double?,
    @Json(name = "ratingImdbVoteCount") val ratingImdbVoteCount: Int?,
    @Json(name = "ratingFilmCritics") val ratingFilmCritics: Double?,
    @Json(name = "ratingFilmCriticsVoteCount") val ratingFilmCriticsVoteCount: Int?,
    @Json(name = "ratingAwait") val ratingAwait: Double?,
    @Json(name = "ratingAwaitCount") val ratingAwaitCount: Int?,
    @Json(name = "ratingRfCritics") val ratingRfCritics: Double?,
    @Json(name = "ratingRfCriticsVoteCount") val ratingRfCriticsVoteCount: Int?,
    @Json(name = "webUrl") val webUrl: String,
    @Json(name = "year") val year: Int,
    @Json(name = "filmLength") val filmLength: Int?,
    @Json(name = "slogan") val slogan: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "shortDescription") val shortDescription: String?,
    @Json(name = "editorAnnotation") val editorAnnotation: String?,
    @Json(name = "isTicketsAvailable") val isTicketsAvailable: Boolean,
    @Json(name = "productionStatus") val productionStatus: String?,
    @Json(name = "type") val type: String,
    @Json(name = "ratingMpaa") val ratingMpaa: String?,
    @Json(name = "ratingAgeLimits") val ratingAgeLimits: String?,
    @Json(name = "hasImax") val hasImax: Boolean?,
    @Json(name = "has3D") val has3D: Boolean?,
    @Json(name = "lastSync") val lastSync: String,
    @Json(name = "countries") val countries: List<CountryDto>,
    @Json(name = "genres") val genres: List<GenreDto>,
    @Json(name = "startYear") val startYear: Int?,
    @Json(name = "endYear") val endYear: Int?,
    @Json(name = "serial") val serial: Boolean?,
    @Json(name = "shortFilm") val shortFilm: Boolean?,
    @Json(name = "completed") val completed: Boolean?
)

fun ResponseDetailMovie.mapToDomainMovie(): MovieDetail {
    return MovieDetail(
        movieId = this.kinopoiskId,
        name = this.nameRu ?: this.nameEn ?: "",
        poster = this.posterUrlPreview,
        rating = null,
        year = this.year,
        length = this.filmLength,
        shortDescription = this.shortDescription,
        description = this.description,
        type = this.type,
        ageLimit = this.ratingAgeLimits,
        startYear = this.startYear,
        endYear = this.endYear,
        serial = this.serial,
        genres = this.genres.map { Genre(genre = it.genre) },
        countries = this.countries.map { Country(country = it.country) }
    )
}