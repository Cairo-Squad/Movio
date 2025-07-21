package com.cairosquad.repository.utils.mappers

import com.cairosquad.domain.exception.DUnauthorizedException
import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.DomainJsonParsingException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.repository.utils.exception.ApiException
import com.cairosquad.repository.utils.exception.BadRequestException
import com.cairosquad.repository.utils.exception.ConflictException
import com.cairosquad.repository.utils.exception.DataSourceException
import com.cairosquad.repository.utils.exception.ForbiddenException
import com.cairosquad.repository.utils.exception.NoInternetException
import com.cairosquad.repository.utils.exception.NotFoundException
import com.cairosquad.repository.utils.exception.RepoEmptyResponseException
import com.cairosquad.repository.utils.exception.RepoJsonParsingException
import com.cairosquad.repository.utils.exception.RequestTimeoutException
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.TooManyRequestsException
import com.cairosquad.repository.utils.exception.UnknownDataSourceException
import com.cairosquad.repository.utils.exception.UnauthorizedException

suspend fun <T> tryToCall(execute: suspend () -> T): T {
    return try {
        execute()
    } catch (exception: DataSourceException) {
        throw getDomainExceptionFromDataException(exception)
    }
}


fun getDomainExceptionFromDataException(exception: DataSourceException): MovioException {
    return when (exception) {
        is ApiException -> when (exception) {
            is RequestTimeoutException -> NetworkException(exception.message)
            is TooManyRequestsException -> NetworkException(exception.message)
            is ForbiddenException -> NetworkException(exception.message)
            is ConflictException -> NetworkException(exception.message)
            is BadRequestException -> NetworkException(exception.message)
            is NotFoundException -> UnknownException("Not Found")
            is UnauthorizedException -> DUnauthorizedException(exception.message)
        }
        is ServerException -> NetworkException(exception.message)
        is NoInternetException -> InternetConnectionException(exception.message)
        is UnknownDataSourceException -> UnknownException(exception.message)
        is RepoEmptyResponseException -> DomainEmptyResponseException(exception.message)
        is RepoJsonParsingException -> DomainJsonParsingException(exception.message)
    }
}