package com.cairosquad.viewmodel.onboarding

import app.cash.turbine.test
import com.cairosquad.domain.usecase.OnboardingUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {
    private val onboardingUseCase: OnboardingUseCase = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var onboardingViewModel: OnboardingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        onboardingViewModel = OnboardingViewModel(onboardingUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCompleteOnboarding SHOULD call OnboardingUseCase setOnboardingStateAsCompleted WHEN called`() =
        runTest {
            coEvery { onboardingUseCase.setOnboardingStateAsCompleted() } just runs
            onboardingViewModel.onCompleteOnboarding()
            coVerify { onboardingUseCase.setOnboardingStateAsCompleted() }
        }

    @Test
    fun `onCompleteOnboarding SHOULD navigate to AuthOrHome WHEN success`() = runTest {
        onboardingViewModel.effect.test {
            onboardingViewModel.onCompleteOnboarding()
            assertThat(awaitItem()).isEqualTo(OnboardingEffect.NavigateToAuthOrHome)
            cancelAndIgnoreRemainingEvents()
        }
    }
}