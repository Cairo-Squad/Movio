package com.cairosquad.viewmodel.library.list_content

import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListContentViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var accountUseCase: AccountUseCase
    private lateinit var listContentPager: ListContentPager
    private lateinit var viewModel: ListContentViewModel
    private val listId = 1L

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        accountUseCase = mockk(relaxed = true)
        listContentPager = mockk(relaxed = true)

        coEvery { accountUseCase.getMoviesOfList(listId, 1) } returns listOf(movie1)

        viewModel = ListContentViewModel(listContentPager, accountUseCase, listId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load movies in init`() = runTest {
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.movies).isEqualTo(listOf(movie1.toUiState()))
        assertThat(viewModel.screenState.value.screenStatus)
            .isEqualTo(ListContentScreenState.SectionStatus.SUCCESS)
    }

    @Test
    fun `should handle error when loading movies fails`() = runTest {
        coEvery { accountUseCase.getMoviesOfList(listId, 1) } throws Exception()
        viewModel = ListContentViewModel(listContentPager, accountUseCase, listId)
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.screenStatus)
            .isEqualTo(ListContentScreenState.SectionStatus.ERROR)
    }

//    @Test
//    fun `should delete movie successfully`() = runTest {
//        coEvery { accountUseCase.removeMovieFromList(listId, movie1.id) } returns Unit
//        coEvery { accountUseCase.getMoviesOfList(listId, 1) } returns listOf(movie1)
//
//        viewModel.onMovieDelete(movie1.id)
//
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        assertThat(state.movies).isEmpty()
//        assertThat(state.deletedMoviesIds).contains(movie1.id)
//        assertThat(state.deletedItems).contains("${movie1.id}, movie")
//    }

    @Test
    fun `should set error snack when delete movie fails`() = runTest {
        coEvery { accountUseCase.removeMovieFromList(listId, movie1.id) } throws Exception()

        viewModel.onMovieDelete(movie1.id)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.movies).isEmpty()
        assertThat(state.deletedMoviesIds).contains(movie1.id)
    }

//    @Test
//    fun `should undo last deleted movie`() = runTest {
//        coEvery { accountUseCase.removeMovieFromList(listId, movie1.id) } returns Unit
//        coEvery { accountUseCase.addMovieToList(listId, movie1.id) } returns Unit
//        coEvery { accountUseCase.getMoviesOfList(listId, 1) } returns listOf(movie1)
//
//        viewModel.onMovieDelete(movie1.id)
//        viewModel.onUndoClick()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.deletedMoviesIds).isEmpty()
//        assertThat(viewModel.screenState.value.deletedItems).isEmpty()
//    }

    @Test
    fun `should refresh list`() = runTest {
        val newMovie = movie1.copy(id = 2, title = "Inception")
        coEvery { accountUseCase.getMoviesOfList(listId, 1) } returns listOf(newMovie)

        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.movies).isEqualTo(listOf(newMovie.toUiState()))
    }

    private companion object {
        val genre1 = Genre(id = 1, name = "Action")

        val movie1 = Movie(
            id = 1,
            title = "The Dark Knight",
            rating = 4.0f,
            posterPath = "/img1.jpg",
            genres = listOf(genre1),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )
    }
}