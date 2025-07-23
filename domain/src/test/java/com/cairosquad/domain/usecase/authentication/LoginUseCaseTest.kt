package com.cairosquad.domain.usecase.authentication

import com.cairosquad.domain.repository.LoginRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class LoginUseCaseTest {
    private val loginRepository: LoginRepository = mockk(relaxed = true)
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        loginUseCase = LoginUseCase(loginRepository)
    }

    @Test
    fun `login SHOULD call loginRepository with correct credentials`() = runTest {
        loginUseCase.login(USERNAME, PASSWORD)

        coVerify(exactly = 1) { loginRepository.login(USERNAME, PASSWORD) }
    }

    @Test
    fun `isUserLoggedIn SHOULD call loginRepository`() = runTest {
        loginUseCase.isUserLoggedIn()

        coVerify(exactly = 1) { loginRepository.isUserLoggedIn() }
    }

    @Test
    fun `logout SHOULD call loginRepository`() = runTest {
        loginUseCase.logout()

        coVerify(exactly = 1) { loginRepository.logout() }
    }

    private companion object {
        const val USERNAME = "testUser"
        const val PASSWORD = "testPassword"
    }
}