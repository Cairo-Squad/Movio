package com.cairosquad.viewmodel.auth_gate

import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.OnboardingUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class AuthGateTest {

    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val onboardingUseCase: OnboardingUseCase = mockk(relaxed = true)
    private lateinit var authGate: AuthGate

    @Before
    fun setup() {
        authGate = AuthGate(loginUseCase, onboardingUseCase)
    }

    @Test
    fun `isUserLoggedIn SHOULD return true when user is logged in`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true

        val result = authGate.isUserLoggedIn()

        assertThat(result).isTrue()
    }

    @Test
    fun `isUserLoggedIn SHOULD return false when user is not logged in`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns false

        val result = authGate.isUserLoggedIn()

        assertThat(result).isFalse()
    }

    @Test
    fun `isUserLoggedIn SHOULD return false when an exception happens in use case`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } throws Exception()

        val result = authGate.isUserLoggedIn()

        assertThat(result).isFalse()
    }

    @Test
    fun `isOnboardingStateComplete SHOULD return true when onboarding is complete`() = runTest {
        coEvery { onboardingUseCase.getOnboardingState() } returns true

        val result = authGate.isOnboardingStateComplete()

        assertThat(result).isTrue()
    }

    @Test
    fun `isOnboardingStateComplete SHOULD return false when onboarding is not complete`() =
        runTest {
            coEvery { onboardingUseCase.getOnboardingState() } returns false

            val result = authGate.isOnboardingStateComplete()

            assertThat(result).isFalse()
        }

    @Test
    fun `isOnboardingStateComplete SHOULD return false when an exception happens in use case`() =
        runTest {
            coEvery { onboardingUseCase.getOnboardingState() } throws Exception()

            val result = authGate.isOnboardingStateComplete()

            assertThat(result).isFalse()
        }
}