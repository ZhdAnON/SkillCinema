package com.zhdanon.skillcinema.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhdanon.skillcinema.domain.models.Image
import com.zhdanon.skillcinema.domain.models.MovieGallery

@JsonClass(generateAdapter = true)
data class ResponseImages(
    @Json(name = "items") val images: List<ImageDto>,
    @Json(name = "total") val total: Int,
    @Json(name = "totalPages") val totalPages: Int
)

@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "imageUrl") val imageUrl: String,
    @Json(name = "previewUrl") val previewUrl: String
)

fun ResponseImages.mapToDomain(): MovieGallery {
    return MovieGallery(
        totalImages = total,
        images = images.map {
            Image(
                imageUrl = it.imageUrl,
                previewUrl = it.previewUrl
            )
        }
    )
}