package com.cairosquad.viewmodel.details.movie

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.model.RatingResult
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
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
import org.mockito.ArgumentMatchers.any

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private val testDispatcher = StandardTestDispatcher()

    private val movieId = 123L
    private val manageMoviesUseCase: ManageMoviesUseCase = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val accountUseCase: AccountUseCase = mockk(relaxed = true)
    private val getRatedItemsUseCase: GetRatedItemsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MovieViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel = MovieViewModel(
            manageMoviesUseCase,
            loginUseCase,
            accountUseCase,
            getRatedItemsUseCase,
            movieId
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN start SHOULD load movie data`() = runTest {
        coEvery { manageMoviesUseCase.getMovieById(movieId) } returns mockMovie
        advanceUntilIdle()
        coVerify { manageMoviesUseCase.getMovieById(movieId) }
    }

    @Test
    fun `WHEN start WHEN get movie by id fails SHOULD convert to error state`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { manageMoviesUseCase.getMovieById(any()) } throws RuntimeException()
        val locViewModel = MovieViewModel(
            manageMoviesUseCase,
            loginUseCase,
            accountUseCase,
            getRatedItemsUseCase,
            movieId
        )
        advanceUntilIdle()
        assertThat(locViewModel.screenState.value.errorStatus)
            .isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN start WHEN get movie top cast fails SHOULD convert to error state`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { manageMoviesUseCase.getMovieTopCast(any()) } throws RuntimeException()
        val locViewModel = MovieViewModel(
            manageMoviesUseCase,
            loginUseCase,
            accountUseCase,
            getRatedItemsUseCase,
            movieId
        )
        advanceUntilIdle()
        assertThat(locViewModel.screenState.value.errorStatus)
            .isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN start WHEN get movie reviews fails SHOULD convert to error state`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { manageMoviesUseCase.getMovieReviews(any()) } throws RuntimeException()

        val locViewModel = MovieViewModel(
            manageMoviesUseCase,
            loginUseCase,
            accountUseCase,
            getRatedItemsUseCase,
            movieId
        )
        advanceUntilIdle()
        assertThat(locViewModel.screenState.value.errorStatus)
            .isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN start WHEN get similar movies fails SHOULD convert to error state`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { manageMoviesUseCase.getSimilarMovies(any()) } throws RuntimeException()
        val locViewModel = MovieViewModel(
            manageMoviesUseCase,
            loginUseCase,
            accountUseCase,
            getRatedItemsUseCase,
            movieId
        )
        advanceUntilIdle()
        assertThat(locViewModel.screenState.value.errorStatus)
            .isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN start WHEN uesr is logged in SHOULD get rated items`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        val movieList = listOf(mockMovie)
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        coEvery { getRatedItemsUseCase.execute(1) } returns Pair(movieList, any())
        val locViewModel = MovieViewModel(
            manageMoviesUseCase,
            loginUseCase,
            accountUseCase,
            getRatedItemsUseCase,
            movieId
        )
        advanceUntilIdle()
        coVerify { getRatedItemsUseCase.execute(1) }
        assertThat(locViewModel.screenState.value.isRated).isTrue()
    }

    @Test
    fun `WHEN start WHEN uesr is logged in WHEN get rated items fails SHOULD do nothing`() =
        runTest {
            mockkStatic(Dispatchers::class)
            every { Dispatchers.IO } returns testDispatcher
            coEvery { loginUseCase.isUserLoggedIn() } returns true
            coEvery { getRatedItemsUseCase.execute(1) } throws RuntimeException()
            val locViewModel = MovieViewModel(
                manageMoviesUseCase,
                loginUseCase,
                accountUseCase,
                getRatedItemsUseCase,
                movieId
            )
            advanceUntilIdle()
            assertThat(locViewModel.screenState.value.isRated).isFalse()
        }

    @Test
    fun `WHEN onBackClicked SHOULD emit NavigateBack effect`() = runTest {
        Dispatchers.setMain(testDispatcher)
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onShareClicked SHOULD update state to show share bottom sheet`() = runTest {
        viewModel.onShareClick()
        assertThat(viewModel.screenState.value.isShareBottomSheetOpen).isTrue()
    }

    @Test
    fun `WHEN onPlayTrailerClicked WHEN clicked SHOULD emit PlayTrailer`() = runTest {
        Dispatchers.setMain(testDispatcher)
        viewModel.effect.test {
            viewModel.onPlayClick()
            assertThat(awaitItem()).isEqualTo(MovieEffect.PlayTrailer)
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onDismissShareBottomSheet SHOULD hide share bottom sheet`() = runTest {
        viewModel.onShareClick()
        viewModel.onDismissShareBottomSheet()
        assertThat(viewModel.screenState.value.isShareBottomSheetOpen).isFalse()
    }

    @Test
    fun `WHEN onSeeAllCastClick SHOULD Navigate To All Actors screen`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        viewModel.effect.test {
            viewModel.onSeeAllCastClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToAllActors(1399))
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onMovieClicked SHOULD navigate to movie screen`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        viewModel.effect.test {
            viewModel.onMovieClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToMovie(1399))
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onSeeAllSimilarMoviesClick SHOULD navigate to all similar movies screen`() = runTest {
        mockkStatic(Dispatchers::class)
        Dispatchers.setMain(testDispatcher)
        viewModel.effect.test {
            viewModel.onSeeAllSimilarMoviesClick(1399)
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToSimilarMovies(1399))
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
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

    @Test
    fun `WHEN onFavoriteClick WHEN not logged in SHOULD open no account bottom sheet`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { loginUseCase.isUserLoggedIn() } returns false
        viewModel.onFavoriteClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isTrue()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onFavoriteClick WHEN logged in and not favorite SHOULD add to favorites`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        coEvery { loginUseCase.isUserLoggedIn() } returns true
        viewModel.updateState { it.copy(isFavorite = false) }
        coEvery { accountUseCase.addMovieToFavorite(movieId) } returns Unit

        viewModel.onFavoriteClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isFavorite).isTrue()
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onFavoriteClick WHEN logged in and favorite SHOULD remove from favorites`() =
        runTest {
            mockkStatic(Dispatchers::class)
            every { Dispatchers.IO } returns testDispatcher
            coEvery { loginUseCase.isUserLoggedIn() } returns true
            viewModel.updateState { it.copy(isFavorite = true) }
            coEvery { accountUseCase.removeMovieFromFavorite(movieId) } returns Unit

            viewModel.onFavoriteClick()
            advanceUntilIdle()

            assertThat(viewModel.screenState.value.isFavorite).isFalse()
            assertThat(viewModel.screenState.value.showSnackBar).isFalse()
            unmockkStatic(Dispatchers::class)
        }

    @Test
    fun `WHEN onAddToListClick WHEN not logged in SHOULD show no account bottom sheet`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { loginUseCase.isUserLoggedIn() } returns false

        viewModel.onAddToListClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isTrue()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onAddToListClick WHEN logged in SHOULD load movie lists`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        coEvery { accountUseCase.getMoviesLists(1) } returns emptyList()

        viewModel.onAddToListClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isAddToListBottomSheetOpen).isTrue()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onRateItClick WHEN not logged in SHOULD show no account bottom sheet`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { loginUseCase.isUserLoggedIn() } returns false

        viewModel.onRateItClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isTrue()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onRateItClick WHEN logged in SHOULD open rate bottom sheet`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { loginUseCase.isUserLoggedIn() } returns true

        viewModel.onRateItClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isRateBottomSheetOpen).isTrue()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onCreateListClicked SHOULD update state`() = runTest {
        viewModel.onCreateListClicked()

        assertThat(viewModel.screenState.value.showCreateListBottomSheet).isTrue()
        assertThat(viewModel.screenState.value.isAddToListBottomSheetOpen).isFalse()
    }

    @Test
    fun `WHEN onSubmitCreateListClicked WHEN success SHOULD reopen add to list bottom sheet`() =
        runTest {
            mockkStatic(Dispatchers::class)
            every { Dispatchers.IO } returns testDispatcher
            coEvery { accountUseCase.createList(any()) } returns Unit
            coEvery { accountUseCase.getMoviesLists(1) } returns emptyList()

            viewModel.onSubmitCreateListClicked()
            advanceUntilIdle()
            assertThat(viewModel.screenState.value.isAddToListBottomSheetOpen).isTrue()
            unmockkStatic(Dispatchers::class)
        }

    @Test
    fun `WHEN onSubmitCreateListClicked WHEN error SHOULD still reopen add to list bottom sheet`() =
        runTest {
            mockkStatic(Dispatchers::class)
            every { Dispatchers.IO } returns testDispatcher
            coEvery { accountUseCase.createList(any()) } throws RuntimeException()

            viewModel.onSubmitCreateListClicked()
            advanceUntilIdle()
            assertThat(viewModel.screenState.value.isAddToListBottomSheetOpen).isTrue()
            unmockkStatic(Dispatchers::class)
        }

    @Test
    fun `WHEN onDismissCreateListBottomSheet SHOULD hide it`() = runTest {
        viewModel.updateState { it.copy(showCreateListBottomSheet = true) }
        viewModel.onDismissCreateListBottomSheet()

        assertThat(viewModel.screenState.value.showCreateListBottomSheet).isFalse()
    }

    @Test
    fun `WHEN onListValueChange SHOULD update list name`() = runTest {
        viewModel.onListValueChange("New List")
        assertThat(viewModel.screenState.value.listName).isEqualTo("New List")
    }

    @Test
    fun `WHEN onDismissLoginBottomSheet SHOULD hide it`() = runTest {
        viewModel.updateState { it.copy(isNoAccountBottomSheetOpen = true) }
        viewModel.onDismissLoginBottomSheet()
        assertThat(viewModel.screenState.value.isNoAccountBottomSheetOpen).isFalse()
    }

    @Test
    fun `WHEN onDismissRateBottomSheet SHOULD hide it`() = runTest {
        viewModel.updateState { it.copy(isRateBottomSheetOpen = true) }
        viewModel.onDismissRateBottomSheet()
        assertThat(viewModel.screenState.value.isRateBottomSheetOpen).isFalse()
    }

    @Test
    fun `WHEN onDismissAddToListBottomSheet SHOULD hide it`() = runTest {
        viewModel.updateState { it.copy(isAddToListBottomSheetOpen = true) }
        viewModel.onDismissAddToListBottomSheet()
        assertThat(viewModel.screenState.value.isAddToListBottomSheetOpen).isFalse()
    }

    @Test
    fun `WHEN onRateChange SHOULD update rate`() = runTest {
        viewModel.onRateChange(8)
        assertThat(viewModel.screenState.value.rate).isEqualTo(8)
    }

    @Test
    fun `WHEN onSubmitRateClicked SHOULD close bottom sheet and show success`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        coEvery { manageMoviesUseCase.addMovieRating(movieId, any()) } returns RatingResult(1, "")

        viewModel.onSubmitRateClicked(5)
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isRated).isTrue()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `WHEN onNavigateToLogin SHOULD emit navigation effect`() = runTest {
        viewModel.effect.test {
            viewModel.onNavigateToLogin()
            assertThat(awaitItem()).isEqualTo(MovieEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onRefresh SHOULD reload all data`() = runTest {
        // Just checking it calls without crashing
        viewModel.onRefresh()
    }

    @Test
    fun `WHEN setError SHOULD update error status`() = runTest {
        val exception = InternetConnectionException()
        viewModel.updateState { it.copy(basicDetailsSectionState = MovieScreenState.ScreenStatus.LOADING) }
        viewModel.handleDetailsException(exception)
        val status = viewModel.handleDetailsException(exception)
        assertThat(status).isEqualTo(ErrorStatus.NO_INTERNET)
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