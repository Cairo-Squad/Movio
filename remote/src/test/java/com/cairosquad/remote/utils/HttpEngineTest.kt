package com.cairosquad.remote.utils

import io.ktor.client.engine.okhttp.OkHttpEngine
import org.junit.Assert.assertTrue
import org.junit.Test

class HttpEngineTest {
    @Test
    fun provide_should_return_OkHttpEngine() {
        val engine = HttpEngine.provide()
        assertTrue(engine is OkHttpEngine)
    }
}