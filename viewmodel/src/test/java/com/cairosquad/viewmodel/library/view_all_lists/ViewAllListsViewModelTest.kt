package com.cairosquad.viewmodel.library.view_all_lists

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModelTest.MainDispatcherRule
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
class ViewAllListsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val accountUseCase: AccountUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ViewAllListsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {

        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)

        every { Dispatchers.IO } returns testDispatcher

        coEvery { accountUseCase.getMoviesLists(any()) } returns emptyList()
        coEvery { accountUseCase.getSeriesLists(any()) } returns emptyList()
        viewModel = ViewAllListsViewModel(accountUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `onRefresh SHOULD reload lists`() = runTest {
        coEvery { accountUseCase.getMoviesLists(1) } returns emptyList()
        coEvery { accountUseCase.getSeriesLists(1) } returns emptyList()

        viewModel.onRefresh()
        advanceTimeBy(500)
        advanceUntilIdle()

        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.isRefreshing).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onNavigateBack SHOULD send effect for navigating back`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        advanceUntilIdle()
        viewModel.effect.test {
            viewModel.onNavigateBack()
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(ViewAllListsEffect.OnNavigateBack)
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `onSeriesListClicked SHOULD send effect for navigate to seriesList`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        advanceUntilIdle()
        val listId = 123L
        val listName = "Pixelise"

        viewModel.effect.test {
            viewModel.onSeriesListClicked(listId, listName)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(ViewAllListsEffect.OnSeriesListClicked(listId, listName))
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `onMovieListClicked SHOULD send effect for navigate to moviesList`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        advanceUntilIdle()
        val listId = 123L
        val listName = "Pixelise"

        viewModel.effect.test {
            viewModel.onMovieListClicked(listId, listName)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(ViewAllListsEffect.OnMovieListClicked(listId, listName))
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `onCreateNewListClicked SHOULD show create list bottom sheet`() = runTest {
        viewModel.onCreateNewListClicked()
        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.isCreateListBottomSheetVisible).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddListClicked SHOULD show create list bottom sheet`() = runTest {
        viewModel.onAddListClicked()
        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.showCreateListBottomSheet).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddListClicked SHOULD set listName to empty`() = runTest {
        viewModel.onAddListClicked()
        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.listName).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDismissCreateListBottomSheet SHOULD dismiss create list bottom sheet`() = runTest {
        viewModel.onDismissCreateListBottomSheet()

        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.showCreateListBottomSheet).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }
}