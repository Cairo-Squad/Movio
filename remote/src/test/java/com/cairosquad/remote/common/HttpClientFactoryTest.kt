package com.cairosquad.remote.common

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.test.Test
import io.ktor.client.request.get
import com.google.common.truth.Truth.assertThat
import io.ktor.client.call.body

class HttpClientFactoryTest {
    @Test
    fun `create should return client with default application json content type`() = runTest {
        val engine = MockEngine { request ->
            assertThat(request.headers[HttpHeaders.ContentType])
                .isEqualTo(ContentType.Application.Json.toString())
            respond("{}", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val client = HttpClientFactory.create(engine)
        client.get("https://any/")
    }

    @Test
    fun `create should ignore unknown json keys`() = runTest {
        val payload = """{ "name":"ok", "extra":"will‑be‑ignored" }"""
        val engine = MockEngine {
            respond(
                payload, HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClientFactory.create(engine)
        val dto = client.get("https://any/").body<SimpleDto>()
        assertThat(dto.name).isEqualTo("ok")
    }

    @Serializable
    data class SimpleDto(val name: String)
}