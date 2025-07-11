package com.cairosquad.viewmodel.search

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class SearchUiEvent {
    data class ErrorHappened(val errorStatus: ErrorStatus) : SearchUiEvent()
}