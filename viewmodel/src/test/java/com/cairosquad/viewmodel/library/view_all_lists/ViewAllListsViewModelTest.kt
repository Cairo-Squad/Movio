package com.cairosquad.viewmodel.library.view_all_lists

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.MediaList
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
class ViewAllListsViewModelTest {

    private val accountUseCase: AccountUseCase = mockk()
    private lateinit var viewModel: ViewAllListsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewAllListsViewModel(accountUseCase)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init SHOULD load movie and series lists`() = runTest {
        val movies = listOf(MediaList(id = 1, name = "Movie List 1", 10))
        val series = listOf(MediaList(id = 2, name = "Series List 1", 10))

        coEvery { accountUseCase.getMoviesLists(1) } returns movies
        coEvery { accountUseCase.getSeriesLists(1) } returns series

        viewModel = ViewAllListsViewModel(accountUseCase)
        advanceUntilIdle()

        viewModel.screenState.test {
            skipItems(1) // skip initial state if needed
            val state = awaitItem()
            assertThat(state.movieLists).isNotEmpty()
            assertThat(state.seriesLists).isNotEmpty()
        }
    }

    @Test
    fun `onRefresh SHOULD reload lists`() = runTest {
        coEvery { accountUseCase.getMoviesLists(1) } returns emptyList()
        coEvery { accountUseCase.getSeriesLists(1) } returns emptyList()


        viewModel.onRefresh()
        advanceTimeBy(500)
        advanceUntilIdle()

        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.isRefreshing).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error in getMoviesLists SHOULD set screenStatus to ERROR`() = runTest {
        coEvery { accountUseCase.getMoviesLists(1) } throws RuntimeException()
        coEvery { accountUseCase.getSeriesLists(1) } returns emptyList()


        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.screenStatus).isEqualTo(ViewAllListsScreenState.SectionStatus.ERROR)
        }
    }

    @Test
    fun `onNavigateBack SHOULD send effect for navigating back`() = runTest {

        viewModel.effect.test {
            viewModel.onNavigateBack()
            assertThat(awaitItem()).isEqualTo(ViewAllListsEffect.OnNavigateBack)
        }
    }

    @Test
    fun `onSeriesListClicked SHOULD send effect for navigate to seriesList`() = runTest {
        val listId = 123L
        val listName = "Pixelise"
        viewModel.effect.test {
            viewModel.onSeriesListClicked(listId, listName)
            assertThat(awaitItem()).isEqualTo(ViewAllListsEffect.OnSeriesListClicked(listId, listName))
        }
    }

    @Test
    fun `onMovieListClicked SHOULD send effect for navigate to moviesList`() = runTest {
        val listId = 123L
        val listName = "Pixelise"
        viewModel.effect.test {
            viewModel.onMovieListClicked(listId, listName)
            assertThat(awaitItem()).isEqualTo(ViewAllListsEffect.OnMovieListClicked(listId, listName))
        }
    }

    @Test
    fun `onCreateNewListClicked SHOULD show create list bottom sheet`() = runTest {
        viewModel.onCreateNewListClicked()
        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.isCreateListBottomSheetVisible).isTrue()
        }
    }

    @Test
    fun `onAddListClicked SHOULD show create list bottom sheet`() = runTest {
        viewModel.onAddListClicked()
        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.showCreateListBottomSheet).isTrue()
        }
    }

    @Test
    fun `onAddListClicked SHOULD set listName to empty`() = runTest {
        viewModel.onAddListClicked()
        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.listName).isEmpty()
        }
    }

    @Test
    fun `onDismissCreateListBottomSheet SHOULD dismiss create list bottom sheet`() = runTest {
        viewModel.onDismissCreateListBottomSheet()

        viewModel.screenState.test {
            val state = awaitItem()
            assertThat(state.showCreateListBottomSheet).isFalse()
        }
    }
}
