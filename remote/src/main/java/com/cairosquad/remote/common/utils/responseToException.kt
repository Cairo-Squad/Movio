package com.cairosquad.remote.common.utils

import com.cairosquad.repository.search.data_source.remote.dto.ErrorDto
import com.cairosquad.repository.utils.exception.RequestTimeoutException
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.TooManyRequestsException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.cairosquad.repository.utils.exception.UnknownDataSourceException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToException(
    response: HttpResponse
): T {
    when (response.status.value) {
        in 200..299 -> {
            try {
                return response.body<T>()
            } catch (e: NoTransformationFoundException) {
                throw e
            }
        }
        /*TODO(Add more cases)*/
        401 -> throw UnauthorizedException(response.body<ErrorDto>().statusMessage ?: "")
        408 -> throw RequestTimeoutException(response.body<ErrorDto>().statusMessage ?: "")
        429 -> throw TooManyRequestsException(response.body<ErrorDto>().statusMessage ?: "")
        in 500..599 -> throw ServerException(response.body<ErrorDto>().statusMessage ?: "")
        else -> throw UnknownDataSourceException(response.body<ErrorDto>().statusMessage ?: "")
    }
}