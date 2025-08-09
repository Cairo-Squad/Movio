package com.cairosquad.viewmodel.library.view_all_favorite

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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

        accountUseCase = mockk(relaxed = true)
        viewModel = ViewAllFavoriteViewModel(accountUseCase)
    }

    @Test
    fun `should fetch data in the init function`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { accountUseCase.getFavoriteMovies(page) } returns listOf(movie)
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)

        viewModel = ViewAllFavoriteViewModel(accountUseCase)

        assertThat(viewModel.screenState.value.movies).isEqualTo(listOf(movie.toUiState()))
        assertThat(viewModel.screenState.value.series).isEqualTo(listOf(series.toUiState()))
    }

    @Test
    fun `should handle error when fetching movies`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { accountUseCase.getFavoriteMovies(page) } throws Exception()
        coEvery { accountUseCase.getFavoriteSeries(page) } returns listOf(series)

        viewModel = ViewAllFavoriteViewModel(accountUseCase)

        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

//    @Test
//    fun `should send effect OnNavigateBack when onBackClicked is called`() = runTest {
//        viewModel.onBackClicked()
//        viewModel.effect.test {
//            assertThat(awaitItem()).isEqualTo(ViewAllFavoriteEffect.OnNavigateBack)
//        }
//    }

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