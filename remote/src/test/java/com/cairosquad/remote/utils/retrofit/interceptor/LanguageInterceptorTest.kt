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
import java.util.Locale

class LanguageInterceptorTest {

    private lateinit var chain: Interceptor.Chain
    private lateinit var interceptor: LanguageInterceptor

    @Before
    fun setup() {
        chain = mockk()
        interceptor = LanguageInterceptor()
    }

    @Test
    fun `interceptor should add language query parameter with system locale`() {
        // Given
        val url = "https://api.example.com/resource".toHttpUrl()
        val request = Request.Builder().url(url).build()

        every { chain.request() } returns request
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            val languageParam = request.url.queryParameter("language")
            assertThat(languageParam).isEqualTo(Locale.getDefault().toLanguageTag())
            mockk<Response>(relaxed = true)
        }

        // When
        interceptor.intercept(chain)

        // Then
        verify(exactly = 1) {
            chain.proceed(withArg {
                assertThat(it.url.queryParameter("language")).isEqualTo(Locale.getDefault().toLanguageTag())
                assertThat(it.url.encodedPath).isEqualTo("/resource")
            })
        }
    }
}