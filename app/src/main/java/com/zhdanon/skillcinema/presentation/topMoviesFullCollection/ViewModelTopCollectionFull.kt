package com.zhdanon.skillcinema.presentation.topMoviesFullCollection

import androidx.paging.PagingData
import com.zhdanon.skillcinema.core.BaseViewModel
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.usecasesAPI.GetTopCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelTopCollectionFull @Inject constructor(
    private val topUseCase: GetTopCollectionsUseCase
) : BaseViewModel() {


    lateinit var topMoviesPaging: Flow<PagingData<Movie>>

    fun setCategory(type: String) {
        topMoviesPaging = topUseCase.executePaging(topType = type)
    }
}