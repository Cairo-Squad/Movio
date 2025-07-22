package com.cairosquad.remote.utils.retrofit

import com.cairosquad.repository.utils.exception.BadRequestException
import com.cairosquad.repository.utils.exception.ConflictException
import com.cairosquad.repository.utils.exception.ForbiddenException
import com.cairosquad.repository.utils.exception.NoInternetException
import com.cairosquad.repository.utils.exception.NotFoundException
import com.cairosquad.repository.utils.exception.RepoJsonParsingException
import com.cairosquad.repository.utils.exception.RequestTimeoutException
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.TooManyRequestsException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.cairosquad.repository.utils.exception.UnknownDataSourceException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertFailsWith

class SafeCallApiTest {

    private fun createHttpException(code: Int, message: String): HttpException {
        val errorJson = """{"status_message":"$message"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())
        val response = Response.error<Any>(code, errorBody)
        return HttpException(response)
    }

    @Test
    fun `should throw BadRequestException on 400`() = runTest {
        val exception = assertFailsWith<BadRequestException> {
            safeCallApi { throw createHttpException(400, "Bad Request") }
        }
        assert(exception.message == "Bad Request")
    }

    @Test
    fun `should throw UnauthorizedException on 401`() = runTest {
        val exception = assertFailsWith<UnauthorizedException> {
            safeCallApi { throw createHttpException(401, "Unauthorized") }
        }
        assert(exception.message == "Unauthorized")
    }

    @Test
    fun `should throw ForbiddenException on 403`() = runTest {
        val exception = assertFailsWith<ForbiddenException> {
            safeCallApi { throw createHttpException(403, "Forbidden") }
        }
        assert(exception.message == "Forbidden")
    }

    @Test
    fun `should throw NotFoundException on 404`() = runTest {
        val exception = assertFailsWith<NotFoundException> {
            safeCallApi { throw createHttpException(404, "Not Found") }
        }
        assert(exception.message == "Not Found")
    }

    @Test
    fun `should throw RequestTimeoutException on 408`() = runTest {
        val exception = assertFailsWith<RequestTimeoutException> {
            safeCallApi { throw createHttpException(408, "Timeout") }
        }
        assert(exception.message == "Timeout")
    }

    @Test
    fun `should throw ConflictException on 409`() = runTest {
        val exception = assertFailsWith<ConflictException> {
            safeCallApi { throw createHttpException(409, "Conflict") }
        }
        assert(exception.message == "Conflict")
    }

    @Test
    fun `should throw TooManyRequestsException on 429`() = runTest {
        val exception = assertFailsWith<TooManyRequestsException> {
            safeCallApi { throw createHttpException(429, "Too Many Requests") }
        }
        assert(exception.message == "Too Many Requests")
    }

    @Test
    fun `should throw ServerException on 500`() = runTest {
        val exception = assertFailsWith<ServerException> {
            safeCallApi { throw createHttpException(500, "Server Error") }
        }
        assert(exception.message == "Server Error")
    }

    @Test
    fun `should throw UnknownDataSourceException on unknown HTTP code`() = runTest {
        val exception = assertFailsWith<UnknownDataSourceException> {
            safeCallApi { throw createHttpException(777, "Mystery Error") }
        }
        assert(exception.message == "Mystery Error")
    }

    @Test
    fun `should throw NoInternetException on IOException`() = runTest {
        val exception = assertFailsWith<NoInternetException> {
            safeCallApi { throw IOException("No connection") }
        }
        assert(exception.message == "No connection")
    }

    @Test
    fun `should throw RepoJsonParsingException on SerializationException`() = runTest {
        val exception = assertFailsWith<RepoJsonParsingException> {
            safeCallApi { throw SerializationException("Invalid JSON") }
        }
        assert(exception.message == "Invalid JSON")
    }

    @Test
    fun `should propagate unknown exception`() = runTest {
        class CustomException : Exception("Something weird")

        val exception = assertFailsWith<CustomException> {
            safeCallApi { throw CustomException() }
        }
        assert(exception.message == "Something weird")
    }

    @Test
    fun `should return result when no exception thrown`() = runTest {
        val result = safeCallApi { "Success" }
        assert(result == "Success")
    }
}