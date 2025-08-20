package com.cairosquad.viewmodel.library.view_all_favorite

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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
class ViewAllFavoriteViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var accountUseCase: AccountUseCase
    private lateinit var viewModel: ViewAllFavoriteViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        accountUseCase = mockk(relaxed = true)
        viewModel = ViewAllFavoriteViewModel(accountUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should fetch data in the init function`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(movie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)

        viewModel = ViewAllFavoriteViewModel(accountUseCase)

        assertThat(viewModel.screenState.value.movies).isEqualTo(listOf(movie.toUiState()))
        assertThat(viewModel.screenState.value.series).isEqualTo(listOf(series.toUiState()))
    }

    @Test
    fun `should handle error when fetching movies`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } throws Exception()
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)

        viewModel = ViewAllFavoriteViewModel(accountUseCase)

        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should send effect OnNavigateBack when onBackClicked is called`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(ViewAllFavoriteEffect.OnNavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should send effect OnMovieClicked when onMovieClicked is called`() = runTest {
        viewModel.effect.test {
            viewModel.onMovieClick(movie.id)
            assertThat(awaitItem()).isEqualTo(ViewAllFavoriteEffect.OnMovieClicked(movie.id))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should send effect OnSeriesClicked when onSeriesClicked is called`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClick(series.id)
            assertThat(awaitItem()).isEqualTo(ViewAllFavoriteEffect.OnSeriesClicked(series.id))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should remove movie on success when onMovieDelete is called`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(movie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns emptyList()
        coEvery { accountUseCase.removeMovieFromFavorite(movie.id) } returns Unit


        viewModel.onMovieDelete(movie.id)

        assertThat(viewModel.screenState.value.movies).isEmpty()
        assertThat(viewModel.screenState.value.deletedMoviesIds).contains(movie.id)
        assertThat(viewModel.screenState.value.deletedItems).contains("${movie.id}, movie")
        assertThat(viewModel.screenState.value.isProcessSuccess).isTrue()

    }

    @Test
    fun `should handle error when removing movie fails`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(movie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns emptyList()
        coEvery { accountUseCase.removeMovieFromFavorite(movie.id) } throws Exception()


        viewModel.onMovieDelete(movie.id)

        assertThat(viewModel.screenState.value.movies).isEmpty()
        assertThat(viewModel.screenState.value.deletedMoviesIds).contains(movie.id)
        assertThat(viewModel.screenState.value.deletedItems).contains("${movie.id}, movie")
        assertThat(viewModel.screenState.value.isProcessSuccess).isFalse()

    }

    @Test
    fun `should remove series on success when onSeriesDelete is called`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns emptyList()
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)
        coEvery { accountUseCase.removeSeriesFromFavorite(series.id) } returns Unit

        viewModel.onSeriesDelete(series.id)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.series).isEmpty()
        assertThat(viewModel.screenState.value.deletedSeriesIds).contains(series.id)
        assertThat(viewModel.screenState.value.deletedItems).contains("${series.id}, series")
        assertThat(viewModel.screenState.value.isProcessSuccess).isTrue()
    }

    @Test
    fun `should handle error when removing series fails`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns emptyList()
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)
        coEvery { accountUseCase.removeSeriesFromFavorite(series.id) } throws Exception()


        viewModel.onSeriesDelete(series.id)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.series).isEmpty()
        assertThat(viewModel.screenState.value.deletedSeriesIds).contains(series.id)
        assertThat(viewModel.screenState.value.deletedItems).contains("${series.id}, series")
        assertThat(viewModel.screenState.value.isProcessSuccess).isFalse()
    }

    @Test
    fun `should refresh favorites when onRefresh is called`() = runTest {
        val newMovie = movie.copy(id = 2, title = "Inception")
        val newSeries = series.copy(id = 2, title = "Stranger Things")
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(newMovie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(newSeries)

        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.movies).isEqualTo(listOf(newMovie.toUiState()))
        assertThat(viewModel.screenState.value.series).isEqualTo(listOf(newSeries.toUiState()))
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(ViewAllFavoriteScreenState.SectionStatus.SUCCESS)

    }

    @Test
    fun `should restore deleted movie when onUndoClicked is called`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(movie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns emptyList()
        coEvery { accountUseCase.removeMovieFromFavorite(movie.id) } returns Unit
        coEvery { accountUseCase.addMovieToFavorite(movie.id) } returns Unit

        viewModel.onMovieDelete(movie.id)

        viewModel.onUndoClick()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.movies).isEqualTo(listOf(movie.toUiState()))
        assertThat(viewModel.screenState.value.deletedMoviesIds).isEmpty()
        assertThat(viewModel.screenState.value.deletedItems).isEmpty()
    }

    @Test
    fun `should restore deleted series when onUndoClicked is called`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns emptyList()
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)
        coEvery { accountUseCase.removeSeriesFromFavorite(series.id) } returns Unit
        coEvery { accountUseCase.addSeriesToFavorite(series.id) } returns Unit

        viewModel.onSeriesDelete(series.id)

        viewModel.onUndoClick()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.series).isEqualTo(listOf(series.toUiState()))
        assertThat(viewModel.screenState.value.deletedSeriesIds).isEmpty()
        assertThat(viewModel.screenState.value.deletedItems).isEmpty()
    }

    @Test
    fun `should handle error when restoring item fails`() = runTest {
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(movie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns emptyList()
        coEvery { accountUseCase.removeMovieFromFavorite(movie.id) } returns Unit
        coEvery { accountUseCase.addMovieToFavorite(movie.id) } throws Exception()

        viewModel.onMovieDelete(movie.id)

        viewModel.onUndoClick()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.isProcessSuccess).isFalse()

    }

    private companion object {
        val page = 1

        val genre1 = Genre(id = 1, name = "Action")
        val genre2 = Genre(id = 2, name = "Drama")

        val movie = Movie(
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

        val series = Series(
            id = 1,
            title = "Breaking Bad",
            rating = 4.8f,
            posterPath = "/series1.jpg",
            trailerPath = "",
            genres = listOf(genre2),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 5
        )
    }
}