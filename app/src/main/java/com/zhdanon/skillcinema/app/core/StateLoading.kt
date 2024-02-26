package com.zhdanon.skillcinema.app.core

sealed class StateLoading {
    object Default : StateLoading()
    object Loading : StateLoading()
    object Success : StateLoading()
    class Error(message: String) : StateLoading()
}