package com.cairosquad.repository.common.exception

import com.cairosquad.domain.search.exception.MovioException
import com.cairosquad.domain.search.exception.NetworkException
import com.cairosquad.domain.search.exception.UnknownException

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
    }
}