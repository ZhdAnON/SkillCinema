package com.zhdanon.skillcinema.presentation.detailMovie

import androidx.lifecycle.viewModelScope
import com.zhdanon.skillcinema.core.BaseViewModel
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.domain.models.MovieDetail
import com.zhdanon.skillcinema.domain.models.Staff
import com.zhdanon.skillcinema.domain.usecasesAPI.GetDetailMovieUseCase
import com.zhdanon.skillcinema.domain.usecasesAPI.GetStaffsByMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelDetailMovie @Inject constructor(
    private val getFilmByIdUseCase: GetDetailMovieUseCase,
    private val getStaffsByMovieUseCase: GetStaffsByMovieUseCase
) : BaseViewModel() {

    private var currentMovieId: Int = 0

    private val _movieDetailInfo = MutableStateFlow<MovieDetail?>(null)
    val movieDetailInfo = _movieDetailInfo.asStateFlow()

    private val _staffs = MutableStateFlow<List<Staff>>(emptyList())
    val staffs = _staffs.asStateFlow()

    fun getFilmById(movieId: Int) {
        currentMovieId = movieId
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadState.value = StateLoading.Loading
                _movieDetailInfo.value = getFilmByIdUseCase.execute(movieId)
                _staffs.value = getStaffsByMovieUseCase.execute(movieId)
                _loadState.value = StateLoading.Success
            } catch (e: Exception) {
                _loadState.value = StateLoading.Error(e.message.toString())
            }
        }
    }
}