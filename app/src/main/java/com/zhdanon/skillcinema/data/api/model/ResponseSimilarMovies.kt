package com.zhdanon.skillcinema.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.MovieSimilar

@JsonClass(generateAdapter = true)
data class ResponseSimilarMovies(
    @Json(name = "total") val total: Int,
    @Json(name = "items") val items: List<MovieSimilarDto>
)

@JsonClass(generateAdapter = true)
data class MovieSimilarDto(
    @Json(name = "filmId") val movieId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "relationType") val relationType: String
)

fun ResponseSimilarMovies.mapToDomain(): MovieSimilar {
    return MovieSimilar(
        total = this.total,
        movies = this.items.map { movie ->
            Movie(
                movieId = movie.movieId,
                name = movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: "",
                poster = movie.posterUrl,
                rating = null,
                genres = emptyList()
            )
        }
    )
}