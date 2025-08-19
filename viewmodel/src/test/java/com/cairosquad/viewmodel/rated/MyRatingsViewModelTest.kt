package com.cairosquad.viewmodel.rated

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import io.mockk.coVerify
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
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MyRatingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getRatedItemsUseCase: GetRatedItemsUseCase
    private lateinit var manageSeriesUseCase: ManageSeriesUseCase
    private lateinit var manageMoviesUseCase: ManageMoviesUseCase

    private lateinit var viewModel: MyRatingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getRatedItemsUseCase = mockk(relaxed = true)
        manageSeriesUseCase = mockk(relaxed = true)
        manageMoviesUseCase = mockk(relaxed = true)

        viewModel = MyRatingsViewModel(
            getRatedItemsUseCase,
            manageSeriesUseCase,
            manageMoviesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackPressed sends NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackPressed()
            assertEquals(MyRatingsEffect.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onMovieClicked sends NavigateToMovieDetails`() = runTest {
        viewModel.effect.test {
            viewModel.onMovieClick(42L)
            assertEquals(MyRatingsEffect.NavigateToMovieDetails(42L), awaitItem())
        }
    }

    @Test
    fun `onSeriesClicked sends NavigateToSeriesDetails`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClick(42L)
            assertEquals(MyRatingsEffect.NavigateToSeriesDetails(42L), awaitItem())
        }
    }

    @Test
    fun `onUndoClicked with movie restores rating`() = runTest {
        viewModel.updateState { it.copy(deletedItems = listOf("movie, 10, 3")) }

        viewModel.onUndoClick()
        advanceUntilIdle()

        coVerify { manageMoviesUseCase.addMovieRating(10L, 6f) }
    }
}

