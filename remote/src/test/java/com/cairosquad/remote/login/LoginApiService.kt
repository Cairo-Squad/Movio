package com.cairosquad.remote.login

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class LoginApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: LoginApiService

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val requestTokenJson = """{"request_token":"token"}"""
    private val sessionIdJson = """{"session_id":"session"}"""

    @Before
    fun setup() {
        mockWebServer = MockWebServer().apply { start() }

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(LoginApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `createRequestToken SHOULD send GET and parse RequestTokenResponse`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(requestTokenJson))

        val result = api.createRequestToken()
        assertEquals("token", result.requestToken)

        val recorded = mockWebServer.takeRequest()
        assertEquals("GET", recorded.method)
        assertEquals("GET /authentication/token/new HTTP/1.1", recorded.requestLine)
    }

    @Test
    fun `authenticateRequestToken SHOULD send POST with correct queries and parse RequestTokenResponse`() =
        runTest {
            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(requestTokenJson))
            val user = "user"
            val pass = "pass"
            val req = "reqToken"

            val result = api.authenticateRequestToken(user, pass, req)
            assertEquals("token", result.requestToken)

            val recorded = mockWebServer.takeRequest()
            assertEquals("POST", recorded.method)
            assertEquals(
                "/authentication/token/validate_with_login",
                recorded.requestUrl?.encodedPath
            )
            assertEquals(user, recorded.requestUrl?.queryParameter("username"))
            assertEquals(pass, recorded.requestUrl?.queryParameter("password"))
            assertEquals(req, recorded.requestUrl?.queryParameter("request_token"))
        }

    @Test
    fun `createSessionId SHOULD send GET with request_token query and parse SessionIdResponse`() =
        runTest {
            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(sessionIdJson))
            val req = "reqToken"

            val result = api.createSessionId(req)
            assertEquals("session", result.sessionId)

            val recorded = mockWebServer.takeRequest()
            assertEquals("GET", recorded.method)
            assertEquals("/authentication/session/new", recorded.requestUrl?.encodedPath)
            assertEquals(req, recorded.requestUrl?.queryParameter("request_token"))
        }

    @Test
    fun `non-200 response SHOULD throw HttpException`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        assertFailsWith<HttpException> {
            api.createRequestToken()
        }
    }
}
