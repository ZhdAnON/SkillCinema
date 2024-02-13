package com.zhdanon.skillcinema.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel(){
    protected val _loadState =
        MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadState = _loadState.asStateFlow()
}