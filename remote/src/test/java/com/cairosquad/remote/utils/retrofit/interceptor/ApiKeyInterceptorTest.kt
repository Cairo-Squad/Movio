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

class ApiKeyInterceptorTest {

    private lateinit var interceptor: ApiKeyInterceptor
    private lateinit var chain: Interceptor.Chain

    @Before
    fun setup() {
        interceptor = ApiKeyInterceptor("test-key")
        chain = mockk(relaxed = true)
    }

    @Test
    fun `interceptor should append api_key query parameter`() {
        // Given
        val originalUrl = "https://api.example.com/resource".toHttpUrl()
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            val url = request.url
            assertThat(url.queryParameter("api_key")).isEqualTo("test-key")
            mockk<Response>(relaxed = true)
        }

        // When
        interceptor.intercept(chain)

        // Then
        verify(exactly = 1) {
            chain.proceed(withArg {
                assertThat(it.url.queryParameter("api_key")).isEqualTo("test-key")
                assertThat(it.url.encodedPath).isEqualTo("/resource")
            })
        }
    }
}
