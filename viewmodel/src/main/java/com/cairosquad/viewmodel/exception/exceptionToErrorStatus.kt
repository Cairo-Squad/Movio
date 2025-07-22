package com.cairosquad.viewmodel.exception

import com.cairosquad.domain.exception.DUnauthorizedException
import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.exception.DomainJsonParsingException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException

fun exceptionToErrorStatus(e: MovioException): ErrorStatus {
    return when (e) {
        is NetworkException -> ErrorStatus.NETWORK_ERROR

        is UnknownException -> ErrorStatus.UNKNOWN_ERROR

        is InternetConnectionException -> ErrorStatus.NO_INTERNET

        is DUnauthorizedException -> ErrorStatus.UNAUTHORIZED

        is DomainEmptyResponseException -> ErrorStatus.EMPTY

        is DomainJsonParsingException -> ErrorStatus.PARSING_ERROR
    }
}