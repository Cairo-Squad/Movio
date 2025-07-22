package com.cairosquad.repository.login

import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.repository.LoginRepository
import com.cairosquad.repository.login.data_source.local.LocalLoginDataSource
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.login.data_source.remote.dto.RequestTokenResponse
import com.cairosquad.repository.utils.exception.NoInternetException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LoginRepositoryImplTest {
    private val remoteLoginDataSource: RemoteLoginDataSource = mockk(relaxed = true)
    private val localLoginDataSource: LocalLoginDataSource = mockk(relaxed = true)

    private lateinit var loginRepository: LoginRepository

    @Before
    fun setup() {
        loginRepository = LoginRepositoryImpl(remoteLoginDataSource, localLoginDataSource)
    }

    @Test
    fun `login SHOULD request new token from remote data source`() = runTest {
        loginRepository.login(USERNAME, PASSWORD)
        coVerify(exactly = 1) { remoteLoginDataSource.createRequestToken() }
    }

    @Test
    fun `login SHOULD map exception to correct domain exception when request token fails`() =
        runTest {
            coEvery { remoteLoginDataSource.createRequestToken() } throws NoInternetException()

            assertFailsWith<InternetConnectionException> {
                loginRepository.login(USERNAME, PASSWORD)
            }
        }

    @Test
    fun `login SHOULD authenticate request token with correct credentials`() = runTest {
        coEvery { remoteLoginDataSource.createRequestToken() } returns requestTokenResponse

        loginRepository.login(USERNAME, PASSWORD)
        coVerify(exactly = 1) {
            remoteLoginDataSource.authenticateRequestToken(
                username = USERNAME,
                password = PASSWORD,
                requestToken = requestTokenResponse.requestToken ?: ""
            )
        }
    }

    @Test
    fun `login SHOULD authenticate empty request token when request token is null`() = runTest {
        coEvery { remoteLoginDataSource.createRequestToken() } returns requestTokenResponse.copy(
            requestToken = null
        )

        loginRepository.login(USERNAME, PASSWORD)
        coVerify(exactly = 1) {
            remoteLoginDataSource.authenticateRequestToken(
                username = USERNAME,
                password = PASSWORD,
                requestToken = ""
            )
        }
    }

    @Test
    fun `login SHOULD map exception to correct domain exception when authenticate token fails`() =
        runTest {
            coEvery { remoteLoginDataSource.createRequestToken() } returns requestTokenResponse
            coEvery {
                remoteLoginDataSource.authenticateRequestToken(
                    any(),
                    any(),
                    any()
                )
            } throws NoInternetException()

            assertFailsWith<InternetConnectionException> {
                loginRepository.login(USERNAME, PASSWORD)
            }
        }

    @Test
    fun `login SHOULD get session id using request token from remote data source`() = runTest {
        coEvery { remoteLoginDataSource.createRequestToken() } returns requestTokenResponse
        coEvery {
            remoteLoginDataSource.authenticateRequestToken(
                USERNAME,
                PASSWORD,
                requestTokenResponse.requestToken ?: ""
            )
        } returns requestTokenResponse
        loginRepository.login(USERNAME, PASSWORD)
        coVerify(exactly = 1) {
            remoteLoginDataSource.createSessionId(
                requestTokenResponse.requestToken ?: ""
            )
        }
    }

    @Test
    fun `login SHOULD map exception to correct domain exception when get session id fails`() =
        runTest {
            coEvery { remoteLoginDataSource.createRequestToken() } returns requestTokenResponse
            coEvery {
                remoteLoginDataSource.authenticateRequestToken(
                    USERNAME,
                    PASSWORD,
                    requestTokenResponse.requestToken ?: ""
                )
            } returns requestTokenResponse
            coEvery {
                remoteLoginDataSource.createSessionId(
                    requestTokenResponse.requestToken ?: ""
                )
            } throws NoInternetException()

            assertFailsWith<InternetConnectionException> {
                loginRepository.login(USERNAME, PASSWORD)
            }
        }

    @Test
    fun `login SHOULD call remote data source with correct username, password`() = runTest {
        val username = "testUser"
        val password = "testPassword"
    }


    @Test
    fun `logout SHOULD call local data source with empty string`() = runTest {
        loginRepository.logout()
        coVerify(exactly = 1) { localLoginDataSource.saveSessionId("") }
    }

    companion object {
        private const val USERNAME = "testUser"
        private const val PASSWORD = "testPassword"
        private val requestTokenResponse = RequestTokenResponse("testToken")
    }

    @Test
    fun `logout SHOULD map exception to correct domain exception when save session id fails`() =
        runTest {
            coEvery { localLoginDataSource.saveSessionId("") } throws NoInternetException()
            assertFailsWith<InternetConnectionException> {
                loginRepository.logout()
            }
        }
}