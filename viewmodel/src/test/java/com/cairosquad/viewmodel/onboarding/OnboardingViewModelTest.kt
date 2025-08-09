package com.cairosquad.viewmodel.onboarding

import app.cash.turbine.test
import com.cairosquad.domain.usecase.OnboardingUseCase
import com.cairosquad.viewmodel.details.movie.MovieViewModelTest.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val onboardingUseCase: OnboardingUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var onboardingViewModel: OnboardingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        onboardingViewModel = OnboardingViewModel(onboardingUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCompleteOnboarding SHOULD call OnboardingUseCase setOnboardingStateAsCompleted WHEN called`() =
        runTest {
            mockkStatic(Dispatchers::class)
            every { Dispatchers.IO } returns testDispatcher
            coEvery { onboardingUseCase.setOnboardingStateAsCompleted() } just runs
            onboardingViewModel.onCompleteOnboarding()
            advanceUntilIdle()
            coVerify { onboardingUseCase.setOnboardingStateAsCompleted() }
            unmockkStatic(Dispatchers::class)
        }

    @Test
    fun `onCompleteOnboarding SHOULD navigate to AuthOrHome WHEN success`() = runTest {
        Dispatchers.setMain(testDispatcher)
        advanceUntilIdle()
        onboardingViewModel.effect.test {
            onboardingViewModel.onCompleteOnboarding()
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(OnboardingEffect.NavigateToAuthOrHome)
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }
}