package com.zhdanon.skillcinema.app.presentation.topMoviesFullCollection

import androidx.paging.PagingData
import com.zhdanon.skillcinema.app.core.AppResources
import com.zhdanon.skillcinema.app.core.BaseViewModel
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.usecasesAPI.GetTopCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelTopCollectionFull @Inject constructor(
    private val topUseCase: GetTopCollectionsUseCase,
    private val appResources: AppResources
) : BaseViewModel() {


    lateinit var topMoviesPaging: Flow<PagingData<Movie>>

    fun setCategory(type: String) {
        topMoviesPaging = topUseCase.executePaging(topType = type)
    }

    fun getCollectionLabel(type: String) = appResources.getCategoryTitle(type)
}