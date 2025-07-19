package com.cairosquad.repository.utils.exception

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DataSourceExceptionTest {
    @Test
    fun `UnknownDataSourceException is instance of DataSourceException`() {
        val exception = UnknownDataSourceException("Unknown error")
        assertThat(exception).isInstanceOf(DataSourceException::class.java)
        assertThat(exception.message).isEqualTo("Unknown error")
    }

    @Test
    fun `ServerException is instance of DataSourceException`() {
        val exception = ServerException("Server down")
        assertThat(exception).isInstanceOf(DataSourceException::class.java)
        assertThat(exception.message).isEqualTo("Server down")
    }

    @Test
    fun `RequestTimeoutException is instance of ApiException and DataSourceException`() {
        val exception = RequestTimeoutException("Request timed out")
        assertThat(exception).isInstanceOf(ApiException::class.java)
        assertThat(exception).isInstanceOf(DataSourceException::class.java)
        assertThat(exception.message).isEqualTo("Request timed out")
    }

    @Test
    fun `TooManyRequestsException is instance of ApiException and DataSourceException`() {
        val exception = TooManyRequestsException("Rate limit exceeded")
        assertThat(exception).isInstanceOf(ApiException::class.java)
        assertThat(exception).isInstanceOf(DataSourceException::class.java)
        assertThat(exception.message).isEqualTo("Rate limit exceeded")
    }

    @Test
    fun `UnauthorizedException is instance of DataSourceException`() {
        val exception = UnauthorizedException("Unauthorized access")
        assertThat(exception).isInstanceOf(DataSourceException::class.java)
        assertThat(exception.message).isEqualTo("Unauthorized access")
    }

    @Test
    fun `NoInternetException is instance of DataSourceException`() {
        val exception = NoInternetException("No internet connection")
        assertThat(exception).isInstanceOf(DataSourceException::class.java)
        assertThat(exception.message).isEqualTo("No internet connection")
    }

    @Test
    fun `Default message is empty string`() {
        val unknown = UnknownDataSourceException()
        val server = ServerException()
        val timeout = RequestTimeoutException()
        val tooMany = TooManyRequestsException()
        val unauthorized = UnauthorizedException()
        val noInternet = NoInternetException()

        assertThat(unknown.message).isEmpty()
        assertThat(server.message).isEmpty()
        assertThat(timeout.message).isEmpty()
        assertThat(tooMany.message).isEmpty()
        assertThat(unauthorized.message).isEmpty()
        assertThat(noInternet.message).isEmpty()
    }

    @Test
    fun `All exceptions can be caught as DataSourceException`() {
        val exceptions = listOf<DataSourceException>(
            UnknownDataSourceException("1"),
            ServerException("2"),
            RequestTimeoutException("3"),
            TooManyRequestsException("4"),
            UnauthorizedException("5"),
            NoInternetException("6")
        )

        exceptions.forEach {
            assertThat(it).isInstanceOf(DataSourceException::class.java)
        }
    }
}