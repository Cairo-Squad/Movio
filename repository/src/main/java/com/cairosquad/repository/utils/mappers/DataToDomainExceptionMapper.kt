package com.cairosquad.repository.utils.mappers

import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.repository.utils.exception.ApiException
import com.cairosquad.repository.utils.exception.BadRequestException
import com.cairosquad.repository.utils.exception.ConflictException
import com.cairosquad.repository.utils.exception.DataSourceException
import com.cairosquad.repository.utils.exception.EmptyResponseException
import com.cairosquad.repository.utils.exception.ForbiddenException
import com.cairosquad.repository.utils.exception.JsonParsingException
import com.cairosquad.repository.utils.exception.NoInternetException
import com.cairosquad.repository.utils.exception.NotFoundException
import com.cairosquad.repository.utils.exception.RequestTimeoutException
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.TooManyRequestsException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.cairosquad.repository.utils.exception.UnknownDataSourceException

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
                is BadRequestException -> TODO()
                is ConflictException -> TODO()
                is ForbiddenException -> TODO()
                is NotFoundException -> TODO()
                is UnauthorizedException -> TODO()
            }
        }

        is ServerException -> NetworkException(exception.message)
        is UnauthorizedException -> NetworkException(exception.message) // TODO: Handle unauthorized exception when authentication feature is implemented
        is UnknownDataSourceException -> UnknownException(exception.message)
        is NoInternetException -> InternetConnectionException(exception.message)
        is EmptyResponseException -> TODO()
        is JsonParsingException -> TODO()
    }
}