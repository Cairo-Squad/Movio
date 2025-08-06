package com.cairosquad.viewmodel.home

import app.cash.turbine.test
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
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
class HomeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var manageMoviesUseCase: ManageMoviesUseCase
    private lateinit var manageSeriesUseCase: ManageSeriesUseCase

    private lateinit var unifiedMediaPager: UnifiedMediaPager
    private lateinit var accountUseCase: AccountUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        manageMoviesUseCase = mockk(relaxed = true)
        manageSeriesUseCase = mockk(relaxed = true)
        unifiedMediaPager = mockk(relaxed = true)
        accountUseCase = mockk(relaxed = true)
        viewModel = HomeViewModel(
            manageMoviesUseCase,
            manageSeriesUseCase,
            accountUseCase,
            unifiedMediaPager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load popular movies and series on initialization`() = runTest {

        val popularMovies = listOf(movie1)
        val popularSeries = listOf(series1)
        coEvery {
            manageMoviesUseCase.getPopularMovies(
                page = 1,
                genreId = null
            )
        } returns popularMovies
        coEvery {
            manageSeriesUseCase.getPopularSeries(
                page = 1,
                genreId = null
            )
        } returns popularSeries

        viewModel = HomeViewModel(
            manageMoviesUseCase,
            manageSeriesUseCase,
            accountUseCase,
            unifiedMediaPager
        )
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.popularMovies).isEqualTo(popularMovies.map { it.toHomeMediaUiState() })
        assertThat(state.popularSeries).isEqualTo(popularSeries.map { it.toHomeMediaUiState() })
    }

    @Test
    fun `should load genres on initialization`() = runTest {

        val movieGenres = listOf(Genre(id = 1, name = "Action"))
        val seriesGenres = listOf(Genre(id = 2, name = "Drama"))
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns movieGenres
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns seriesGenres

        viewModel = HomeViewModel(
            manageMoviesUseCase,
            manageSeriesUseCase,
            accountUseCase,
            unifiedMediaPager
        )
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.genres).contains(HomeScreenState.GenreUiState.defaultGenre)
        assertThat(state.genres).contains(HomeScreenState.GenreUiState(1, "Action"))
        assertThat(state.genres).contains(HomeScreenState.GenreUiState(2, "Drama"))
    }

//    @Test
//    fun `should load section data for TOP_RATING on initialization`() = runTest {
//
//        val topRatingMovies = listOf(movie1)
//        val topRatingSeries = listOf(series1)
//        coEvery {
//            manageMoviesUseCase.getPopularMovies(
//                page = 1,
//                genreId = null
//            )
//        } returns topRatingMovies
//        coEvery {
//            manageSeriesUseCase.getPopularSeries(
//                page = 1,
//                genreId = null
//            )
//        } returns topRatingSeries
//
//        viewModel = HomeViewModel(
//            manageMoviesUseCase,
//            manageSeriesUseCase,
//            unifiedMediaPager
//        )
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        val section = state.sections[MediaContentType.TOP_RATING]
//        assertThat(section?.movies).isEqualTo(topRatingMovies.map { it.toHomeMediaUiState() })
//        assertThat(section?.series).isEqualTo(topRatingSeries.map { it.toHomeMediaUiState() })
//    }

    @Test
    fun `should set error status when fetching popular media fails`() = runTest {

        coEvery {
            manageMoviesUseCase.getPopularMovies(
                page = 1,
                genreId = null
            )
        } throws NetworkException()
        coEvery {
            manageSeriesUseCase.getPopularSeries(
                page = 1,
                genreId = null
            )
        } returns emptyList()

        viewModel = HomeViewModel(
            manageMoviesUseCase,
            manageSeriesUseCase,
            accountUseCase,
            unifiedMediaPager
        )
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.dataRequestStatus).isEqualTo(HomeScreenState.DateRequestStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `should navigate to profile when onClickProfile is called`() = runTest {

        viewModel.effect.test {
            viewModel.onClickProfile()

            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateToProfile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to media details when onClickMedia is called`() = runTest {

        viewModel.effect.test {
            viewModel.onClickMedia(mediaId = 123, isMovie = true)

            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateMediaDetails(123, true))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to see all screen when onClickSeeAll is called`() = runTest {

        viewModel.effect.test {
            viewModel.onClickSeeAll(MediaContentType.TOP_RATING, MediaType.MOVIES)

            assertThat(awaitItem()).isEqualTo(
                HomeEffect.NavigateToSeeAllScreen(
                    MediaContentType.TOP_RATING,
                    MediaType.MOVIES
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

//    @Test
//    fun `should update selected tab and fetch media by genre when CATEGORIES tab is selected`() =
//        runTest {
//
//            val movies = listOf(movie1)
//            val series = listOf(series1)
//            coEvery { manageMoviesUseCase.getAllMovies(page = 1, genreId = null) } returns movies
//            coEvery { manageSeriesUseCase.getAllSeries(page = 1, genreId = null) } returns series
//
//            viewModel.onClickTab(HomeScreenState.Tab.CATEGORIES.ordinal)
//            advanceUntilIdle()
//
//            val state = viewModel.screenState.value
//            assertThat(state.selectedTab).isEqualTo(HomeScreenState.Tab.CATEGORIES)
//            assertThat(state.categoriesMedia).isEqualTo(movies.map { it.toHomeMediaUiState() } + series.map { it.toHomeMediaUiState() })
//        }
//
//    @Test
//    fun `should update selected genre and fetch media by genre when genre is selected`() = runTest {
//
//        val genreId = 1L
//        val movies = listOf(movie1)
//        val series = listOf(series1)
//        coEvery { manageMoviesUseCase.getAllMovies(page = 1, genreId = genreId) } returns movies
//        coEvery { manageSeriesUseCase.getAllSeries(page = 1, genreId = genreId) } returns series
//        viewModel.updateState {
//            it.copy(
//                genres = listOf(
//                    HomeScreenState.GenreUiState.defaultGenre,
//                    HomeScreenState.GenreUiState(genreId, "Action")
//                )
//            )
//        }
//
//        viewModel.onGenreSelected(1)
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        assertThat(state.selectedGenreIndex).isEqualTo(1)
//        assertThat(state.categoriesMedia).isEqualTo(movies.map { it.toHomeMediaUiState() } + series.map { it.toHomeMediaUiState() })
//    }

//    @Test
//    fun `should sort categories media by POPULARITY when sorting type is selected`() = runTest {
//
//        val genreId = 1L
//        val movies = listOf(movie1)
//        val series = listOf(series1)
//        coEvery {
//            manageMoviesUseCase.getAllMovies(
//                page = 1,
//                genreId = genreId,
//                SortType.POPULAR
//            )
//        } returns movies
//        coEvery {
//            manageSeriesUseCase.getAllSeries(
//                page = 1,
//                genreId = genreId,
//                SortType.POPULAR
//            )
//        } returns series
//        viewModel.updateState {
//            it.copy(
//                genres = listOf(
//                    HomeScreenState.GenreUiState.defaultGenre,
//                    HomeScreenState.GenreUiState(genreId, "Action")
//                ),
//                selectedGenreIndex = 1
//            )
//        }
//
//        viewModel.onSortingSelected(HomeScreenState.SortingType.POPULARITY)
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        assertThat(state.selectedSortingType).isEqualTo(HomeScreenState.SortingType.POPULARITY)
//        assertThat(state.categoriesMedia).isEqualTo(movies.map { it.toHomeMediaUiState() } + series.map { it.toHomeMediaUiState() })
//    }
//
//    @Test
//    fun `should handle error when fetching section data fails`() = runTest {
//
//        coEvery {
//            manageMoviesUseCase.getTopRatingMovies(
//                page = 1,
//                genreId = null
//            )
//        } throws NetworkException()
//        coEvery {
//            manageSeriesUseCase.getTopRatingSeries(
//                page = 1,
//                genreId = null
//            )
//        } returns emptyList()
//
//        viewModel = HomeViewModel(
//            manageMoviesUseCase,
//            manageSeriesUseCase,
//            unifiedMediaPager
//        )
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        assertThat(state.screenStatus).isEqualTo(HomeScreenState.ScreenStatus.FAILED)
//        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
//    }

    private companion object {
        val movie1 = Movie(
            id = 1,
            title = "The Dark Knight",
            rating = 4.0f,
            posterPath = "/img.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )

        val series1 = Series(
            id = 1,
            title = "Series",
            rating = 3.5f,
            posterPath = "/img.jpg",
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 1
        )
    }
}