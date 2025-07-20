package com.cairosquad.remote.utils.retrofit

import com.cairosquad.repository.utils.exception.BadRequestException
import com.cairosquad.repository.utils.exception.ConflictException
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
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import retrofit2.Response

suspend inline fun <reified T> safeCallApi(
    crossinline call: suspend () -> Response<T>
): T {
    return try {
        val response = call()
        if (response.isSuccessful) {
            response.body() ?: throw EmptyResponseException()
        } else {
            val errorBody = response.errorBody()?.string()
            val message = extractErrorMessage(errorBody)

            when (response.code()) {
                400 -> throw BadRequestException(message)
                401 -> throw UnauthorizedException(message)
                403 -> throw ForbiddenException(message)
                404 -> throw NotFoundException(message)
                408 -> throw RequestTimeoutException(message)
                409 -> throw ConflictException(message)
                429 -> throw TooManyRequestsException(message)
                in 500..599 -> throw ServerException(message)
                else -> throw UnknownDataSourceException(message)
            }
        }
    } catch (e: IOException) {
        throw NoInternetException(e.message ?: "")
    } catch (e: SerializationException) {
        throw JsonParsingException(e.message ?: "Json parsing failed")
    } catch (e: Exception) {
        throw e
    }
}


