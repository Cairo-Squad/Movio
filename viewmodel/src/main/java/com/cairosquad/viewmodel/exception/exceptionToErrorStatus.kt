package com.cairosquad.viewmodel.exception

import com.cairosquad.domain.exception.UnauthorizedActionException
import com.cairosquad.domain.exception.NoDataException
import com.cairosquad.domain.exception.ParsingException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException

fun exceptionToErrorStatus(e: MovioException): ErrorStatus {
    return when (e) {
        is NetworkException -> ErrorStatus.NETWORK_ERROR

        is UnknownException -> ErrorStatus.UNKNOWN_ERROR

        is InternetConnectionException -> ErrorStatus.NO_INTERNET

        is UnauthorizedActionException -> ErrorStatus.UNAUTHORIZED

        is NoDataException -> ErrorStatus.EMPTY

        is ParsingException -> ErrorStatus.PARSING_ERROR
    }
}