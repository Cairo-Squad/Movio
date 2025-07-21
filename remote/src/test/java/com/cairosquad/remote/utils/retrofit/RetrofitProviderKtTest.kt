package com.cairosquad.remote.utils.retrofit


import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.utils.retrofit.interceptor.ApiKeyInterceptor
import com.cairosquad.remote.utils.retrofit.interceptor.AuthInterceptor
import com.cairosquad.remote.utils.retrofit.interceptor.LanguageInterceptor
import com.google.common.truth.Truth.assertThat
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test

class RetrofitProviderTest {

    @Test
    fun `retrofitProvider should return Retrofit with correct base URL`() {
        // When
        val retrofit = retrofitProvider { "token" }

        // Then
        assertThat(retrofit.baseUrl().toString()).isEqualTo(BuildConfig.BASE_URL)
    }

    @Test
    fun `retrofitProvider should include all expected interceptors`() {
        // When
        val retrofit = retrofitProvider { "token" }
        val client = retrofit.callFactory() as okhttp3.OkHttpClient
        val interceptors = client.interceptors

        // Then
        assertThat(interceptors.any { it is ApiKeyInterceptor }).isTrue()
        assertThat(interceptors.any { it is AuthInterceptor }).isTrue()
        assertThat(interceptors.any { it is LanguageInterceptor }).isTrue()
        assertThat(interceptors.any {
            it is HttpLoggingInterceptor && it.level == HttpLoggingInterceptor.Level.BODY
        }).isTrue()
    }

    @Test
    fun `AuthInterceptor should receive the correct token from provider`() {
        // Given
        var capturedToken = "token"
        val tokenProvider = { capturedToken }

        // When
        val retrofit = retrofitProvider(tokenProvider)
        val client = retrofit.callFactory() as okhttp3.OkHttpClient

        // Then
        val authInterceptor = client.interceptors.find { it is AuthInterceptor }
        assertThat(authInterceptor).isNotNull()
        assertThat(capturedToken).isEqualTo("token")
    }
}
