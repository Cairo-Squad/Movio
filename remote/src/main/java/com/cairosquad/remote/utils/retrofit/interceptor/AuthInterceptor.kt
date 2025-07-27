package com.cairosquad.remote.utils.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicReference

class AuthInterceptor : Interceptor {
    companion object {
        private val token = AtomicReference<String>("")
        fun updateToken(newToken: String) {
            token.set(newToken)
        }
    }
    override fun intercept(chain: Interceptor.Chain): Response {

        val currentToken = token.get()
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
}