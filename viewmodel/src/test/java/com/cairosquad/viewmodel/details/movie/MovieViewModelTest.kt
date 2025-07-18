package com.cairosquad.viewmodel.details.movie

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.usecase.movies.GetMoviesDetailsUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()
    private val movieId = 123L
    private val mockUseCase: GetMoviesDetailsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MovieViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieViewModel(mockUseCase, movieId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init SHOULD load all data sections`() = runTest {
        // Given
        coEvery { mockUseCase.getMovie(movieId) } returns mockMovie
        coEvery { mockUseCase.getMovieTopCast(movieId) } returns emptyList()
        coEvery { mockUseCase.getMovieReviews(movieId) } returns emptyList()
        coEvery { mockUseCase.getSimilarMovies(movieId) } returns emptyList()

        // When
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            mockUseCase.getMovie(movieId)
            mockUseCase.getMovieTopCast(movieId)
            mockUseCase.getMovieReviews(movieId)
            mockUseCase.getSimilarMovies(movieId)
        }
    }

    @Test
    fun `WHEN onBackClicked SHOULD emit NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onShareClicked SHOULD update state to show share bottom sheet`() = runTest {
        viewModel.onShareClick()
        assertThat(viewModel.screenState.value.isShareBottomSheetOpen).isTrue()
    }

    @Test
    fun `WHEN onFavoriteClicked WHEN not logged in SHOULD show login bottom sheet`() = runTest {
        viewModel.onFavoriteClick()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isTrue()
    }

    @Test
    fun `oWHEN nAddToListClicked WHEN not logged in SHOULD show login bottom sheet`() = runTest {
        viewModel.onAddToListClick()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isTrue()
    }

    @Test
    fun `onRateClicked WHEN not logged in SHOULD show login bottom sheet`() = runTest {
        viewModel.onRateItClick()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isTrue()
    }

    @Test
    fun `WHEN onPlayTrailerClicked WHEN clicked SHOULD emit PlayTrailer`() = runTest {
        viewModel.effect.test {
            viewModel.onPlayClick()
            assertThat(awaitItem()).isEqualTo(MovieEffect.PlayTrailer)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onDismissShareBottomSheet SHOULD hide share bottom sheet`() = runTest {
        viewModel.effect.test {
            viewModel.onShareClick()
            viewModel.onDismissShareBottomSheet()
            assertThat(viewModel.screenState.value.isShareBottomSheetOpen).isFalse()
        }
    }

    @Test
    fun `WHEN onSeeAllCastClick SHOULD Navigate To All Actors screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllCastClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToAllActors(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onMovieClicked SHOULD navigate to movie screen`() = runTest {
        viewModel.effect.test {
            viewModel.onMovieClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToMovie(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeeAllSimilarMoviesClick SHOULD navigate to all similar movies screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllSimilarMoviesClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToSimilarMovies(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeeAllSeasonsClicked SHOULD navigate to all seasons screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllReviewsClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToAllReviews(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCopy SHOULD show then hide snackbar after delay`() = runTest {
        viewModel.onCopy("Copied!", true)

        advanceTimeBy(0)
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
    }

    @Test
    fun `WHEN onActorClick SHOULD emit NavigateToArtistDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onActorClick(456L)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToActor(456L))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleDetailsException SHOULD map exceptions correctly`() {
        assertThat(viewModel.handleDetailsException(NetworkException()))
            .isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(viewModel.handleDetailsException(InternetConnectionException()))
            .isEqualTo(ErrorStatus.NO_INTERNET)
        assertThat(viewModel.handleDetailsException(RuntimeException()))
            .isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    companion object {
        private val mockMovie = Movie(
            id = 123,
            title = "Test Series",
            rating = 8.7f,
            posterPath = "/poster.jpg",
            genres = emptyList(),
            releaseDate = 0L,
            overview = "Test overview",
            trailerPath = "",
            runtimeMinutes = 15
        )
    }

    class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}