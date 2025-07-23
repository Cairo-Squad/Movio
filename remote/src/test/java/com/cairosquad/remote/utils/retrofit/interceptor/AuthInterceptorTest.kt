package com.cairosquad.remote.utils.retrofit.interceptor

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

    private lateinit var interceptor: AuthInterceptor
    private lateinit var chain: Interceptor.Chain

    @Before
    fun setup() {
        interceptor = AuthInterceptor("test-id")
        chain = mockk(relaxed = true)
    }

    @Test
    fun `interceptor should add Authorization header if token is provided`() {
        // Given
        val originalUrl = "https://api.example.com/resource".toHttpUrl()
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            val url = request.url
            assertThat(url.queryParameter("session_id")).isEqualTo("test-id")
            mockk<Response>(relaxed = true)
        }

        // When
        interceptor.intercept(chain)

        // Then
        verify(exactly = 1) {
            chain.proceed(withArg {
                assertThat(it.url.queryParameter("session_id")).isEqualTo("test-id")
                assertThat(it.url.encodedPath).isEqualTo("/resource")
            })
        }
    }

    companion object {
        private const val URL = "https://api.example.com"
        private const val AUTHORIZATION = "Authorization"

    }

}