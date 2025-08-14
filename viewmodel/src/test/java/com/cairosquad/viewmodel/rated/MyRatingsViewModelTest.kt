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
            viewModel.onMovieClicked(42L)
            assertEquals(MyRatingsEffect.NavigateToMovieDetails(42L), awaitItem())
        }
    }

    @Test
    fun `onSeriesClicked sends NavigateToSeriesDetails`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClicked(100L)
            assertEquals(MyRatingsEffect.NavigateToSeriesDetails(100L), awaitItem())
        }
    }
//
//    @Test
//    fun `onMovieDelete updates state and calls use case`() = runTest {
//        coEvery { manageMoviesUseCase.deleteMovieRating(55L) } returns Unit
//
//        viewModel.onMovieDelete(55L, 4)
//        advanceUntilIdle()
//
//        coVerify { manageMoviesUseCase.deleteMovieRating(55L) }
//        assertTrue(viewModel.screenState.value.deletedItems.contains("movie, 55, 4"))
//    }
//
//    @Test
//    fun `onSeriesDelete updates state and calls use case`() = runTest {
//        coEvery { manageSeriesUseCase.deleteSeriesRating(77L) } returns Unit
//
//        viewModel.onSeriesDelete(77L, 5)
//        advanceUntilIdle()
//
//        coVerify { manageSeriesUseCase.deleteSeriesRating(77L) }
//        assertTrue(viewModel.screenState.value.deletedItems.contains("tv, 77, 5"))
//    }

    @Test
    fun `onUndoClicked with movie restores rating`() = runTest {
        viewModel.updateState { it.copy(deletedItems = listOf("movie, 10, 3")) }

        viewModel.onUndoClicked()
        advanceUntilIdle()

        coVerify { manageMoviesUseCase.addMovieRating(10L, 6f) }
    }

    @Test
    fun `onUndoClicked with tv restores rating`() = runTest {
        viewModel.updateState { it.copy(deletedItems = listOf("tv, 20, 2")) }

        viewModel.onUndoClicked()
        advanceUntilIdle()

        coVerify { manageSeriesUseCase.addSeriesRating(20L, 4f) }
    }
}