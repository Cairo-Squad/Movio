package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.OnboardingRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class OnboardingUseCaseTest {
    private val onboardingRepository: OnboardingRepository = mockk(relaxed = true)
    private lateinit var onboardingUseCase: OnboardingUseCase

    @Before
    fun setup() {
        onboardingUseCase = OnboardingUseCase(onboardingRepository)
    }

    @Test
    fun `setOnboardingStateAsCompleted SHOULD call repository method`() = runTest {
        coEvery { onboardingRepository.setOnboardingStateAsCompleted() } just Runs

        onboardingUseCase.setOnboardingStateAsCompleted()

        coVerify { onboardingRepository.setOnboardingStateAsCompleted() }
    }

    @Test
    fun `setOnboardingStateAsCompleted SHOULD throw exception when repository fails`() = runTest {
        coEvery { onboardingRepository.setOnboardingStateAsCompleted() } throws Exception("Ex")

        assertThrows<Exception> {
            onboardingUseCase.setOnboardingStateAsCompleted()
        }
    }

    @Test
    fun `getOnboardingState SHOULD return onboarding state from repository if state is true`() =
        runTest {
            coEvery { onboardingRepository.getOnboardingState() } returns true

            val result = onboardingUseCase.getOnboardingState()
            
            assertThat(result).isTrue()
        }

    @Test
    fun `getOnboardingState SHOULD return onboarding state from repository if state is false`() =
        runTest {
            coEvery { onboardingRepository.getOnboardingState() } returns false

            val result = onboardingUseCase.getOnboardingState()

            assertThat(result).isFalse()
        }

    @Test
    fun `getOnboardingState SHOULD throw exception when repository fails`() = runTest {
        coEvery { onboardingRepository.getOnboardingState() } throws Exception("Ex")

        assertThrows<Exception> {
            onboardingUseCase.getOnboardingState()
        }
    }
}