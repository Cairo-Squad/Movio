package com.cairosquad.remote.utils.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlSessionId = originalUrl.newBuilder()
            .addQueryParameter("session_id", tokenProvider())
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlSessionId)
            .build()

        return chain.proceed(newRequest)
    }
}