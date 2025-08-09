package com.cairosquad.viewmodel.library.view_all_history

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewAllHistoryViewModelTest {

    private val accountUseCase: AccountUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ViewAllHistoryViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadHistoryMovies SHOULD set ERROR status on if one of the calls fails`() = runTest {
        coEvery { accountUseCase.getHistoryMovies(1) } throws RuntimeException("Network error")
        coEvery { accountUseCase.getHistorySeries(1) } returns emptyList()

        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.screenStatus)
                .isEqualTo(ViewAllHistoryScreenState.SectionStatus.ERROR)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadHistoryMovies SHOULD set Error status on if both of the calls failed`() = runTest {
        coEvery { accountUseCase.getHistoryMovies(1) } throws RuntimeException("Network error")
        coEvery { accountUseCase.getHistorySeries(1) } throws RuntimeException("Network error")

        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.screenStatus)
                .isEqualTo(ViewAllHistoryScreenState.SectionStatus.ERROR)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClicked SHOULD send OnNavigateBack effect`() = runTest {
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        viewModel.onBackClicked()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ViewAllHistoryEffect.OnNavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClicked SHOULD send OnMovieClicked effect`() = runTest {
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        viewModel.onMovieClicked(123L)

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ViewAllHistoryEffect.OnMovieClicked(123L))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSeriesClicked SHOULD send OnSeriesClicked effect`() = runTest {
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        viewModel.onSeriesClicked(456L)

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ViewAllHistoryEffect.OnSeriesClicked(456L))
            cancelAndIgnoreRemainingEvents()
        }
    }
}