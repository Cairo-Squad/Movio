package com.cairosquad.remote.common

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpEngine
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.Field

class HttpEngineTest {
    @Test
    fun provide_should_return_OkHttpEngine() {
        val engine = HttpEngine.provide()
        assertTrue(engine is OkHttpEngine)
    }
}