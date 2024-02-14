package com.zhdanon.skillcinema.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.skillcinema.domain.models.Staff

@JsonClass(generateAdapter = true)
data class ResponseStaffsByMovie(
    @Json(name = "staffId") val staffId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "professionKey") val professionKey: String,
    @Json(name = "professionText") val professionText: String,
    @Json(name = "description") val description: String?
)

fun ResponseStaffsByMovie.mapToDomain(): Staff {
    return Staff(
        staffId = this.staffId,
        name = this.nameRu ?: this.nameEn,
        posterUrl = this.posterUrl,
        professionKey = this.professionKey,
        professionText = this.professionText,
        description = this.description
    )
}