package com.cairosquad.remote.common

import io.ktor.client.engine.okhttp.OkHttpEngine
import org.junit.Assert.assertTrue
import org.junit.Test

class HttpEngineTest {
    @Test
    fun `provide should return OkHttpEngine`() {
        val engine = HttpEngine.provide()
        assertTrue(engine is OkHttpEngine)
    }
}