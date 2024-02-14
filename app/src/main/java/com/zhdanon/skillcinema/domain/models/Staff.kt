package com.zhdanon.skillcinema.domain.models

data class Staff(
    val staffId: Int,
    val name: String?,
    val posterUrl: String,
    val professionKey: String,
    val professionText: String,
    val description: String?
)