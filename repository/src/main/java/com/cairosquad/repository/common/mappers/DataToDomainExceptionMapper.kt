package com.cairosquad.repository.common.mappers

import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.repository.common.exception.ApiException
import com.cairosquad.repository.common.exception.DataSourceException
import com.cairosquad.repository.common.exception.NoInternetException
import com.cairosquad.repository.common.exception.RequestTimeoutException
import com.cairosquad.repository.common.exception.ServerException
import com.cairosquad.repository.common.exception.TooManyRequestsException
import com.cairosquad.repository.common.exception.UnauthorizedException
import com.cairosquad.repository.common.exception.UnknownDataSourceException

suspend fun <T> tryToCall(execute: suspend () -> T): T {
    return try {
        execute()
    } catch (exception: DataSourceException) {
        throw getDomainExceptionFromDataException(exception)
    }
}

private fun getDomainExceptionFromDataException(exception: DataSourceException): MovioException {
    return when (exception) {
        is ApiException -> {
            when (exception) {
                is RequestTimeoutException -> NetworkException(exception.message)
                is TooManyRequestsException -> NetworkException(exception.message)
            }
        }

        is ServerException -> NetworkException(exception.message)
        is UnauthorizedException -> NetworkException(exception.message) // TODO: Handle unauthorized exception when authentication feature is implemented
        is UnknownDataSourceException -> UnknownException(exception.message)
        is NoInternetException -> InternetConnectionException(exception.message)
    }
}