package com.zhdanon.skillcinema.presentation.detailMovie

import androidx.lifecycle.viewModelScope
import com.zhdanon.skillcinema.core.BaseViewModel
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.domain.models.MovieDetail
import com.zhdanon.skillcinema.domain.usecasesAPI.GetDetailMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelDetailMovie @Inject constructor(
    private val getFilmByIdUseCase: GetDetailMovieUseCase
) : BaseViewModel() {

    private var currentMovieId: Int = 0

    private val _filmDetailInfo = MutableStateFlow<MovieDetail?>(null)
    val filmDetailInfo = _filmDetailInfo.asStateFlow()

    fun getFilmById(movieId: Int) {
        currentMovieId = movieId
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadState.value = StateLoading.Loading
                val tempFilm = getFilmByIdUseCase.execute(movieId)
                _filmDetailInfo.value = tempFilm
                _loadState.value = StateLoading.Success
            } catch (e: Exception) {
                _loadState.value = StateLoading.Error(e.message.toString())
            }
        }
    }
}