package com.zhdanon.skillcinema.presentation.adapters

import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.models.Staff

sealed class MyAdapterTypes {
    data class ItemMovie(val movie: Movie): MyAdapterTypes()
    data class ItemMovieStaff(val person: Staff): MyAdapterTypes()
}