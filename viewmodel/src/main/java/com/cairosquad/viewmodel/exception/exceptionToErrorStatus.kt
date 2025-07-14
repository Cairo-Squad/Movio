package com.cairosquad.viewmodel.exception

import com.cairosquad.domain.search.exception.InternetConnectionException
import com.cairosquad.domain.search.exception.MovioException
import com.cairosquad.domain.search.exception.NetworkException
import com.cairosquad.domain.search.exception.UnknownException

fun exceptionToErrorStatus(e: MovioException): ErrorStatus {
    return when (e) {
        is NetworkException -> {
            ErrorStatus.NETWORK_ERROR
        }

        is UnknownException -> {
            ErrorStatus.UNKNOWN_ERROR
        }

        is InternetConnectionException -> {
            ErrorStatus.NO_INTERNET
        }
    }
}