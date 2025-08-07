package com.cairosquad.repository.onboarding

import com.cairosquad.domain.repository.OnboardingRepository
import com.cairosquad.repository.onboarding.data_source.local.OnboardingDataSource
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

class OnboardingRepositoryImplTest {
    private val onboardingDataSource: OnboardingDataSource = mockk(relaxed = true)
    private lateinit var onboardingRepository: OnboardingRepository

    @Before
    fun setup() {
        onboardingRepository = OnboardingRepositoryImpl(onboardingDataSource)
    }

    @Test
    fun `setOnboardingStateAsCompleted SHOULD call data source method`() = runTest {
        coEvery { onboardingRepository.setOnboardingStateAsCompleted() } just Runs

        onboardingRepository.setOnboardingStateAsCompleted()

        coVerify { onboardingRepository.setOnboardingStateAsCompleted() }
    }

    @Test
    fun `setOnboardingStateAsCompleted SHOULD throw exception when data source fails`() = runTest {
        coEvery { onboardingRepository.setOnboardingStateAsCompleted() } throws Exception("Ex")

        assertThrows<Exception> {
            onboardingRepository.setOnboardingStateAsCompleted()
        }
    }

    @Test
    fun `getOnboardingState SHOULD return onboarding state from data source if state is true`() =
        runTest {
            coEvery { onboardingRepository.getOnboardingState() } returns true

            val result = onboardingRepository.getOnboardingState()

            assertThat(result).isTrue()
        }

    @Test
    fun `getOnboardingState SHOULD return onboarding state from data source if state is false`() =
        runTest {
            coEvery { onboardingRepository.getOnboardingState() } returns false

            val result = onboardingRepository.getOnboardingState()

            assertThat(result).isFalse()
        }

    @Test
    fun `getOnboardingState SHOULD throw exception when data source fails`() = runTest {
        coEvery { onboardingRepository.getOnboardingState() } throws Exception("Ex")

        assertThrows<Exception> {
            onboardingRepository.getOnboardingState()
        }
    }
}