package com.cairosquad.repository.utils.mappers

import com.cairosquad.domain.exception.DUnauthorizedException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.repository.utils.exception.NoInternetException
import com.cairosquad.repository.utils.exception.RequestTimeoutException
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.TooManyRequestsException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.cairosquad.repository.utils.exception.UnknownDataSourceException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TryToCallTest {

    @Test
    fun `should return result when execute succeeds`() = runTest {
        val result = tryToCall {
            "Success"
        }

        assertThat(result).isEqualTo("Success")
    }

    @Test
    fun `should throw NetworkException when ApiException is RequestTimeoutException`() = runTest {
        val exception = RequestTimeoutException("Timeout")
        val result = runCatching {
            tryToCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NetworkException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Timeout")
    }

    @Test
    fun `should throw NetworkException when ApiException is TooManyRequestsException`() = runTest {
        val exception = TooManyRequestsException("Too many requests")
        val result = runCatching {
            tryToCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NetworkException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Too many requests")
    }

    @Test
    fun `should throw NetworkException when ServerException occurs`() = runTest {
        val exception = ServerException("Server error")
        val result = runCatching {
            tryToCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NetworkException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Server error")
    }

    @Test
    fun `should throw NetworkException when UnauthorizedException occurs`() = runTest {
        val exception = UnauthorizedException("You are not authorized to perform this action")
        val result = runCatching {
            tryToCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(DUnauthorizedException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("You are not authorized to perform this action")
    }

    @Test
    fun `should throw UnknownException when UnknownDataSourceException occurs`() = runTest {
        val exception = UnknownDataSourceException("Unknown error")
        val result = runCatching {
            tryToCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(UnknownException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Unknown error")
    }

    @Test
    fun `should throw InternetConnectionException when NoInternetException occurs`() = runTest {
        val exception = NoInternetException("No internet")
        val result = runCatching {
            tryToCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(InternetConnectionException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("No internet")
    }
}