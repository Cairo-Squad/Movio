package com.cairosquad.remote.common.utils

import com.cairosquad.remote.common.dto.ErrorDto
import com.cairosquad.remote.common.exceptions.RequestTimeoutException
import com.cairosquad.remote.common.exceptions.ServerException
import com.cairosquad.remote.common.exceptions.TooManyRequestsException
import com.cairosquad.remote.common.exceptions.UnauthorizedException
import com.cairosquad.remote.common.exceptions.UnknownException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
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
        else -> throw UnknownException(response.body<ErrorDto>().statusMessage ?: "")
    }
}