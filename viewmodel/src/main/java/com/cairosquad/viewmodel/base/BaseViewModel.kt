package com.cairosquad.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E>(
    initialState: T
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<T> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<E>()
    val uiEvent = _uiEvent.asSharedFlow()

    protected fun updateState(transform: (T) -> T) {
        _uiState.update { transform(it) }
    }

    protected fun sendEvent(
        event: E,
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {}
    ) {
        viewModelScope.launch {
            onStart()
            _uiEvent.emit(event)
            onEnd()
        }
    }

    protected fun <R> launchWithResult(
        action: suspend () -> R,
        onSuccess: (R) -> Unit,
        onError: (Throwable) -> Unit,
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {}
    ) {
        viewModelScope.launch {
            onStart()
            runCatching { action() }
                .onSuccess(onSuccess)
                .onFailure(onError)
            onEnd()
        }
    }
}