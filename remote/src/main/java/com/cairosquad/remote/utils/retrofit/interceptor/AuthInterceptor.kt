package com.cairosquad.remote.utils.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val currentToken = token
        if (currentToken == "") return chain.proceed(chain.request())

        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlSessionId = originalUrl.newBuilder()
            .addQueryParameter("session_id", currentToken)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlSessionId)
            .build()

        return chain.proceed(newRequest)
    }
    companion object {
        private var token = ""
        fun updateToken(newToken: String) {
            token = newToken
        }
    }
}