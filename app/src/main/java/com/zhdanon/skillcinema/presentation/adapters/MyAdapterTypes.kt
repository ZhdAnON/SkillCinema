package com.zhdanon.skillcinema.presentation.adapters

import com.zhdanon.skillcinema.domain.models.Movie

sealed class MyAdapterTypes {
    data class ItemMovie(val movie: Movie): MyAdapterTypes()
}