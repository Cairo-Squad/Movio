package com.cairosquad.viewmodel.library.view_all_history

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewAllHistoryViewModelTest {

    private val accountUseCase: AccountUseCase = mockk()
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
    fun `init SHOULD load movies and series`() = runTest {
        // Given
        val movies = listOf(Movie(id = 1, title = "Test Movie", 5f, "", emptyList(), "", 0L, 0, ""))
        val series =
            listOf(Series(id = 2, title = "Test Series", 5f, "", "", emptyList(), "", 0, 5))

        coEvery { accountUseCase.getHistoryMovies(1) } returns movies
        coEvery { accountUseCase.getHistorySeries(1) } returns series

        // When
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        // Then
        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.movies).isNotEmpty()
            assertThat(state.series).isNotEmpty()
        }
    }

    @Test
    fun `onRefresh SHOULD reload movies and series`() = runTest {
        val movies = listOf(Movie(id = 1, title = "Test Movie", 5f, "", emptyList(), "", 0L, 0, ""))
        val series =
            listOf(Series(id = 2, title = "Test Series", 5f, "", "", emptyList(), "", 0, 5))

        coEvery { accountUseCase.getHistoryMovies(1) } returns movies
        coEvery { accountUseCase.getHistorySeries(1) } returns series

        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.onRefresh()
        advanceTimeBy(500) // delay in refresh
        advanceUntilIdle()

        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.isRefreshing).isTrue()
        }
    }

    @Test
    fun `loadHistoryMovies SHOULD set ERROR status on if one of the calls fails`() = runTest {
        coEvery { accountUseCase.getHistoryMovies(1) } throws RuntimeException("Network error")
        coEvery { accountUseCase.getHistorySeries(1) } returns emptyList()

        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.screenStatus).isEqualTo(ViewAllHistoryScreenState.SectionStatus.ERROR)
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
            assertThat(state.screenStatus).isEqualTo(ViewAllHistoryScreenState.SectionStatus.ERROR)
        }
    }

    @Test
    fun `onBackClicked SHOULD send OnNavigateBack effect`() = runTest {
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onBackClicked()
            assertThat(awaitItem()).isEqualTo(ViewAllHistoryEffect.OnNavigateBack)
        }
    }

    @Test
    fun `onMovieClicked SHOULD send OnMovieClicked effect`() = runTest {
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onMovieClicked(123L)
            assertThat(awaitItem()).isEqualTo(ViewAllHistoryEffect.OnMovieClicked(123L))
        }
    }

    @Test
    fun `onSeriesClicked SHOULD send OnSeriesClicked effect`() = runTest {
        viewModel = ViewAllHistoryViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onSeriesClicked(456L)
            assertThat(awaitItem()).isEqualTo(ViewAllHistoryEffect.OnSeriesClicked(456L))
        }
    }
}
