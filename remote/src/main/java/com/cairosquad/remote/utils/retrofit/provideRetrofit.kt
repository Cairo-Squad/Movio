package com.cairosquad.remote.utils.retrofit

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.utils.retrofit.interceptor.ApiKeyInterceptor
import com.cairosquad.remote.utils.retrofit.interceptor.AuthInterceptor
import com.cairosquad.remote.utils.retrofit.interceptor.LanguageInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitProvider {
    private val contentType = "application/json".toMediaType()
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    fun create(tokenProvider: () -> String?): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.API_KEY))
            .addInterceptor(AuthInterceptor(tokenProvider))
            .addInterceptor(LanguageInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}