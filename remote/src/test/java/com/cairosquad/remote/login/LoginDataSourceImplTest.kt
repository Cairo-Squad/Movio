package com.cairosquad.remote.login

import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.login.data_source.remote.dto.RequestTokenResponse
import com.cairosquad.repository.login.data_source.remote.dto.SessionIdResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class LoginDataSourceImplTest {

    private val loginApiService: LoginApiService = mockk()
    private lateinit var remoteLoginDataSource: RemoteLoginDataSource

    private val requestRequestToken = RequestTokenResponse(requestToken = "token")
    private val requestSessionId = SessionIdResponse(sessionId = "session")

    @Before
    fun setup() {
        remoteLoginDataSource = RemoteLoginDataSourceImpl(loginApiService)
    }

    @Test
    fun `createRequestToken SHOULD call api and return result`() = runTest {
        coEvery { loginApiService.createRequestToken() } returns requestRequestToken

        val result = remoteLoginDataSource.createRequestToken()

        assertEquals(requestRequestToken, result)
        coVerify(exactly = 1) { loginApiService.createRequestToken() }
    }

    @Test
    fun `authenticateRequestToken SHOULD call api with correct params and return result`() =
        runTest {
            val user = "user"
            val pass = "pass"
            val req = "reqToken"
            coEvery {
                loginApiService.authenticateRequestToken(
                    user,
                    pass,
                    req
                )
            } returns requestRequestToken

            val result = remoteLoginDataSource.authenticateRequestToken(user, pass, req)

            assertEquals(requestRequestToken, result)
            coVerify(exactly = 1) { loginApiService.authenticateRequestToken(user, pass, req) }
        }

    @Test
    fun `createSessionId SHOULD call api with requestToken and return result`() = runTest {
        val req = "reqToken"
        coEvery { loginApiService.createSessionId(req) } returns requestSessionId

        val result = remoteLoginDataSource.createSessionId(req)

        assertEquals(requestSessionId, result)
        coVerify(exactly = 1) { loginApiService.createSessionId(req) }
    }

    @Test
    fun `createRequestToken SHOULD propagate exceptions`() = runTest {
        val ex = RuntimeException("network error")
        coEvery { loginApiService.createRequestToken() } throws ex

        val thrown = assertFailsWith<RuntimeException> {
            remoteLoginDataSource.createRequestToken()
        }

        assertEquals("network error", thrown.message)
    }

    @Test
    fun `authenticateRequestToken SHOULD propagate exceptions`() = runTest {
        val ex = IllegalStateException("auth failed")
        coEvery {
            loginApiService.authenticateRequestToken(any(), any(), any())
        } throws ex

        val thrown = assertFailsWith<IllegalStateException> {
            remoteLoginDataSource.authenticateRequestToken("u", "p", "r")
        }

        assertEquals("auth failed", thrown.message)
    }

    @Test
    fun `createSessionId SHOULD propagate exceptions`() = runTest {
        val ex = IllegalArgumentException("session error")
        coEvery { loginApiService.createSessionId(any()) } throws ex

        val thrown = assertFailsWith<IllegalArgumentException> {
            remoteLoginDataSource.createSessionId("r")
        }

        assertEquals("session error", thrown.message)
    }
}
