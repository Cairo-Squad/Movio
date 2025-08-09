package com.cairosquad.viewmodel.library.view_all_history

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModelTest.MainDispatcherRule
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewAllHistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val accountUseCase: AccountUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ViewAllHistoryViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onRefresh SHOULD set SUCESS status even if there is no movies or series`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        coEvery { accountUseCase.getHistorySeries(1) } returns emptyList()
        coEvery { accountUseCase.getHistoryMovies(1) } returns emptyList()

        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()
        viewModel.onRefresh()

        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.isRefreshing).isFalse()
            advanceTimeBy(500)
            assertThat(viewModel.screenState.value.isRefreshing).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
        unmockkStatic(Dispatchers::class)

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

    @Test
    fun `updateErrorStatus SHOULD set ERROR status and UNKNOWN_ERROR when non-MovioException`() = runTest {
        // Given
        val throwable = IllegalStateException("boom")
        viewModel = ViewAllHistoryViewModel(accountUseCase)

        // When
        viewModel.updateErrorStatus(throwable)

        // Then
        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(ViewAllHistoryScreenState.SectionStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(state.isRefreshing).isFalse()
    }
}