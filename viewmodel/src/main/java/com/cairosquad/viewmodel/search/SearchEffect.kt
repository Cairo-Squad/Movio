package com.cairosquad.viewmodel.search

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class SearchEffect {
    data class ErrorHappened(val message: ErrorStatus) : SearchEffect()
}