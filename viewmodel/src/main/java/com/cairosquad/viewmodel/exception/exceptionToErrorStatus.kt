package com.cairosquad.viewmodel.exception

import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException

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