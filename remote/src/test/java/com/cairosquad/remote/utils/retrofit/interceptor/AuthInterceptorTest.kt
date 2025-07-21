package com.cairosquad.remote.utils.retrofit.interceptor

import com.google.common.net.HttpHeaders.AUTHORIZATION
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test


class AuthInterceptorTest {

    private lateinit var chain: Interceptor.Chain

    @Before
    fun setup() {
        chain = mockk(relaxed = true)
    }

    @Test
    fun `interceptor should add Authorization header if token is provided`() {
        // Given
        val token = "test"
        val interceptor = AuthInterceptor { token }

        val originalRequest = Request.Builder()
            .url(URL.toHttpUrl())
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            assertThat(request.header(AUTHORIZATION)).isEqualTo("Bearer $token")
            mockk<Response>(relaxed = true)
        }

        // When
        interceptor.intercept(chain)

        // Then
        verify(exactly = 1) {
            chain.proceed(withArg {
                assertThat(it.header(AUTHORIZATION)).isEqualTo("Bearer $token")
            })
        }
    }

    @Test
    fun `interceptor should NOT add Authorization header if token is null`() {
        // Given
        val interceptor = AuthInterceptor { null }

        val originalRequest = Request.Builder()
            .url(URL.toHttpUrl())
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            assertThat(request.header(AUTHORIZATION)).isNull()
            mockk<Response>(relaxed = true)
        }

        // When
        interceptor.intercept(chain)

        // Then
        verify(exactly = 1) {
            chain.proceed(withArg {
                assertThat(it.header(AUTHORIZATION)).isNull()
            })
        }
    }

    companion object {
        private const val URL = "https://api.example.com"
        private const val AUTHORIZATION = "Authorization"

    }

}