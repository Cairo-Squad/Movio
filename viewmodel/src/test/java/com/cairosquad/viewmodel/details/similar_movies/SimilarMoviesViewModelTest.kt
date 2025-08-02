package com.cairosquad.viewmodel.details.similar_movies

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace
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
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SimilarMoviesViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var manageMoviesUseCase: ManageMoviesUseCase
    private lateinit var viewModel: SimilarMoviesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        manageMoviesUseCase = mockk(relaxed = true)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = SimilarMoviesViewModel(manageMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should set LOADING then SUCCESS and map movies correctly when fetching similar movies succeeds`() = runTest {
        val movieId = 1L
        val movies = listOf(movie1, movie2)
        coEvery { manageMoviesUseCase.getSimilarMovies(movieId) } returns movies

        viewModel.fetchSimilarMovies(movieId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarMoviesScreenState.ScreenStatus.SUCCESS)
        assertThat(state.movies).isEqualTo(movies.map { it.toUiState() })
        assertThat(state.errorStatus).isNull()
    }

    @Test
    fun `should set LOADING then ERROR and NO_INTERNET when fetching similar movies fails with InternetConnectionException`() = runTest {
        val movieId = 1L
        coEvery { manageMoviesUseCase.getSimilarMovies(movieId) } throws InternetConnectionException()

        viewModel.fetchSimilarMovies(movieId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarMoviesScreenState.ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NO_INTERNET)
        assertThat(state.movies).isEmpty()
    }

    @Test
    fun `should set LOADING then ERROR and NETWORK_ERROR when fetching similar movies fails with NetworkException`() = runTest {
        val movieId = 1L
        coEvery { manageMoviesUseCase.getSimilarMovies(movieId) } throws NetworkException()

        viewModel.fetchSimilarMovies(movieId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarMoviesScreenState.ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(state.movies).isEmpty()
    }

    @Test
    fun `should set LOADING then ERROR and UNKNOWN_ERROR when fetching similar movies fails with generic exception`() = runTest {
        val movieId = 1L
        coEvery { manageMoviesUseCase.getSimilarMovies(movieId) } throws IOException()

        viewModel.fetchSimilarMovies(movieId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarMoviesScreenState.ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(state.movies).isEmpty()
    }

    @Test
    fun `should emit NavigateBack effect when onClickBack is called`() = runTest {
        viewModel.effect.test {
            viewModel.onClickBack()
            assertThat(awaitItem()).isEqualTo(SimilarMoviesEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit NavigateToMovieDetails effect when onMovieClicked is called`() = runTest {
        val movieId = 123L
        viewModel.effect.test {
            viewModel.onMovieClicked(movieId)
            assertThat(awaitItem()).isEqualTo(SimilarMoviesEffect.NavigateToMovieDetails(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should map Movie to SimilarMovieUiState correctly`() {
        val movie = movie1
        val uiState = movie.toUiState()
        assertThat(uiState.id).isEqualTo(movie.id)
        assertThat(uiState.title).isEqualTo(movie.title)
        assertThat(uiState.rating).isEqualTo(movie.rating.roundToFirstDecimalPlace())
        assertThat(uiState.posterUrl).isEqualTo(movie.posterPath)
    }

    private companion object {
        val movie1 = Movie(
            id = 1,
            title = "The Dark Knight",
            rating = 4.0f,
            posterPath = "/img1.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )

        val movie2 = Movie(
            id = 2,
            title = "Inception",
            rating = 4.5f,
            posterPath = "/img2.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )
    }
}