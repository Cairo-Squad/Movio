package com.cairosquad.viewmodel.library.view_all_lists

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.MediaList
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
import kotlinx.coroutines.test.runCurrent
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

        runCurrent()
        assertThat(viewModel.screenState.value.isRefreshing).isTrue()

        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
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
            viewModel.onSeriesListClick(listId, listName)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(
                ViewAllListsEffect.OnSeriesListClicked(
                    listId,
                    listName
                )
            )
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
            viewModel.onMovieListClick(listId, listName)
            advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(
                ViewAllListsEffect.OnMovieListClicked(
                    listId,
                    listName
                )
            )
            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `onCreateNewListClicked SHOULD show create list bottom sheet`() = runTest {
        viewModel.onCreateNewListClick()
        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.isCreateListBottomSheetVisible).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddListClicked SHOULD show create list bottom sheet`() = runTest {
        viewModel.onAddListClick()
        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.showCreateListBottomSheet).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddListClicked SHOULD set listName to empty`() = runTest {
        viewModel.onAddListClick()
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

    @Test
    fun `onListValueChange SHOULD update listName`() = runTest {
        viewModel.onListValueChange("Test test")
        viewModel.screenState.test {
            assertThat(viewModel.screenState.value.listName).isEqualTo("Test test")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSubmitCreateListClicked - success case`() = runTest {
        val movieLists = listOf(MediaList(1, "Movies", mediaCount = 10)).map { it }
        val seriesLists = listOf(MediaList(2, "Series", mediaCount = 10)).map { it }

        coEvery { accountUseCase.createList(any()) } returns Unit
        coEvery { accountUseCase.getMoviesLists(1) } returns movieLists
        coEvery { accountUseCase.getSeriesLists(1) } returns seriesLists

        viewModel.updateState { it.copy(listName = "My List") }
        viewModel.onSubmitCreateListClick()
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.showCreateListBottomSheet).isFalse()
        assertThat(state.listName).isEmpty()
        assertThat(state.isProcessSuccess).isTrue()
        assertThat(state.movieLists.size).isEqualTo(movieLists.size)
        assertThat(state.seriesLists.size).isEqualTo(seriesLists.size)
    }

    @Test
    fun `onSubmitCreateListClicked - error case`() = runTest {
        coEvery { accountUseCase.createList(any()) } throws RuntimeException("Failed")

        viewModel.updateState { it.copy(listName = "My List") }
        viewModel.onSubmitCreateListClick()
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.showCreateListBottomSheet).isFalse()
        assertThat(state.listName).isEmpty()
        assertThat(state.isProcessSuccess).isFalse()
    }

    @Test
    fun `onSubmitCreateListClicked - snackbar hides after delay`() = runTest {
        coEvery { accountUseCase.createList(any()) } returns Unit
        coEvery { accountUseCase.getMoviesLists(1) } returns emptyList()
        coEvery { accountUseCase.getSeriesLists(1) } returns emptyList()

        viewModel.updateState { it.copy(listName = "My List") }
        viewModel.onSubmitCreateListClick()
        advanceUntilIdle()
        advanceTimeBy(2000L)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
    }

    @Test
    fun `updateErrorStatus sets screenStatus to ERROR`() = runTest {
        // When
        viewModel.updateErrorStatus(Throwable("generic error"))

        // Then
        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(ViewAllListsScreenState.SectionStatus.ERROR,)
    }

    @Test
    fun `updateErrorStatus sets UNKNOWN_ERROR for generic Throwable`() = runTest {
        // When
        viewModel.updateErrorStatus(Throwable("generic error"))

        // Then
        val state = viewModel.screenState.value
        assertThat( state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `updateErrorStatus sets isRefreshing to false`() = runTest {
        // Given state was refreshing
        viewModel.updateState { it.copy(isRefreshing = true) }

        // When
        viewModel.updateErrorStatus(Throwable("error"))

        // Then
        val state = viewModel.screenState.value
        assertThat(state.isRefreshing).isFalse()
    }
}