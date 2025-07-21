package com.cairosquad.remote.utils.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

class LanguageInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("language", Locale.getDefault().toLanguageTag())
            .build()

        val request = original.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}