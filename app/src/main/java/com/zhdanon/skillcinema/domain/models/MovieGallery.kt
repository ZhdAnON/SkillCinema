package com.zhdanon.skillcinema.domain.models

data class MovieGallery(
    val totalImages: Int,
    val images: List<Image>
)

data class Image(
    val imageUrl: String,
    val previewUrl: String
)