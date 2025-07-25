package com.cairosquad.viewmodel.home

import app.cash.turbine.test
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.usecase.movies.GetAllMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoviesGenresUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetPopularMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetAllSeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
import com.cairosquad.domain.usecase.series.GetPopularSeriesUseCase
import com.cairosquad.domain.usecase.series.GetSeriesGenresUseCase
import com.cairosquad.domain.usecase.series.GetTopRatingSeriesUseCase
import com.cairosquad.domain.usecase.series.GetTrendingSeriesUseCase
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

    private lateinit var getFreeToWatchMoviesUseCase: GetFreeToWatchMoviesUseCase
    private lateinit var getMoreRecommendedMoviesUseCase: GetMoreRecommendedMoviesUseCase
    private lateinit var getTopRatingMoviesUseCase: GetTopRatingMoviesUseCase
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
    private lateinit var getAiringTodaySeriesUseCase: GetAiringTodaySeriesUseCase
    private lateinit var getMoreRecommendedSeriesUseCase: GetMoreRecommendedSeriesUseCase
    private lateinit var getOnTvSeriesUseCase: GetOnTvSeriesUseCase
    private lateinit var getTopRatingSeriesUseCase: GetTopRatingSeriesUseCase
    private lateinit var getPopularSeriesUseCase: GetPopularSeriesUseCase
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var getMoviesGenresUseCase: GetMoviesGenresUseCase
    private lateinit var getSeriesGenresUseCase: GetSeriesGenresUseCase
    private lateinit var getTrendingSeriesUseCase: GetTrendingSeriesUseCase
    private lateinit var getAllMoviesUseCase: GetAllMoviesUseCase
    private lateinit var getAllSeriesUseCase: GetAllSeriesUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        getFreeToWatchMoviesUseCase = mockk(relaxed = true)
        getMoreRecommendedMoviesUseCase = mockk(relaxed = true)
        getTopRatingMoviesUseCase = mockk(relaxed = true)
        getTrendingMoviesUseCase = mockk(relaxed = true)
        getUpcomingMoviesUseCase = mockk(relaxed = true)
        getNowPlayingMoviesUseCase = mockk(relaxed = true)
        getAiringTodaySeriesUseCase = mockk(relaxed = true)
        getMoreRecommendedSeriesUseCase = mockk(relaxed = true)
        getOnTvSeriesUseCase = mockk(relaxed = true)
        getTopRatingSeriesUseCase = mockk(relaxed = true)
        getPopularSeriesUseCase = mockk(relaxed = true)
        getPopularMoviesUseCase = mockk(relaxed = true)
        getMoviesGenresUseCase = mockk(relaxed = true)
        getSeriesGenresUseCase = mockk(relaxed = true)
        getTrendingSeriesUseCase = mockk(relaxed = true)
        getAllMoviesUseCase = mockk(relaxed = true)
        getAllSeriesUseCase = mockk(relaxed = true)

        viewModel = HomeViewModel(
            getFreeToWatchMoviesUseCase,
            getMoreRecommendedMoviesUseCase,
            getTopRatingMoviesUseCase,
            getTrendingMoviesUseCase,
            getUpcomingMoviesUseCase,
            getNowPlayingMoviesUseCase,
            getAiringTodaySeriesUseCase,
            getMoreRecommendedSeriesUseCase,
            getOnTvSeriesUseCase,
            getTopRatingSeriesUseCase,
            getPopularSeriesUseCase,
            getPopularMoviesUseCase,
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase,
            getAllMoviesUseCase,
            getAllSeriesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load popular movies and series on initialization`() = runTest {
        // Given
        val popularMovies = listOf(movie1)
        val popularSeries = listOf(series1)
        coEvery { getPopularMoviesUseCase.getPopularMovies(page = 1, categoryId = null) } returns popularMovies
        coEvery { getPopularSeriesUseCase.getPopularSeries(page = 1, categoryId = null) } returns popularSeries

        // When
        viewModel = HomeViewModel(
            getFreeToWatchMoviesUseCase,
            getMoreRecommendedMoviesUseCase,
            getTopRatingMoviesUseCase,
            getTrendingMoviesUseCase,
            getUpcomingMoviesUseCase,
            getNowPlayingMoviesUseCase,
            getAiringTodaySeriesUseCase,
            getMoreRecommendedSeriesUseCase,
            getOnTvSeriesUseCase,
            getTopRatingSeriesUseCase,
            getPopularSeriesUseCase,
            getPopularMoviesUseCase,
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase,
            getAllMoviesUseCase,
            getAllSeriesUseCase
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.popularMovies).isEqualTo(popularMovies.map { it.toHomeMediaUiState() })
        assertThat(state.popularSeries).isEqualTo(popularSeries.map { it.toHomeMediaUiState() })
    }

    @Test
    fun `should load genres on initialization`() = runTest {
        // Given

        val movieGenres = listOf(Genre(id = 1, name = "Action"))
        val seriesGenres = listOf(Genre(id = 2, name = "Drama"))
        coEvery { getMoviesGenresUseCase.getMoviesGenres() } returns movieGenres
        coEvery { getSeriesGenresUseCase.getSeriesGenres() } returns seriesGenres

        // When
        viewModel = HomeViewModel(
            getFreeToWatchMoviesUseCase,
            getMoreRecommendedMoviesUseCase,
            getTopRatingMoviesUseCase,
            getTrendingMoviesUseCase,
            getUpcomingMoviesUseCase,
            getNowPlayingMoviesUseCase,
            getAiringTodaySeriesUseCase,
            getMoreRecommendedSeriesUseCase,
            getOnTvSeriesUseCase,
            getTopRatingSeriesUseCase,
            getPopularSeriesUseCase,
            getPopularMoviesUseCase,
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase,
            getAllMoviesUseCase,
            getAllSeriesUseCase
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.genres).contains(HomeScreenState.GenreUiState.defaultGenre)
        assertThat(state.genres).contains(HomeScreenState.GenreUiState(1, "Action"))
        assertThat(state.genres).contains(HomeScreenState.GenreUiState(2, "Drama"))
    }

    @Test
    fun `should load section data for TOP_RATING on initialization`() = runTest {
        // Given
        val topRatingMovies = listOf(movie1)
        val topRatingSeries = listOf(series1)
        coEvery { getTopRatingMoviesUseCase.getTopRatingMovies(page = 1, categoryId = null) } returns topRatingMovies
        coEvery { getTopRatingSeriesUseCase.getTopRatingSeries(page = 1, categoryId = null) } returns topRatingSeries

        // When
        viewModel = HomeViewModel(
            getFreeToWatchMoviesUseCase,
            getMoreRecommendedMoviesUseCase,
            getTopRatingMoviesUseCase,
            getTrendingMoviesUseCase,
            getUpcomingMoviesUseCase,
            getNowPlayingMoviesUseCase,
            getAiringTodaySeriesUseCase,
            getMoreRecommendedSeriesUseCase,
            getOnTvSeriesUseCase,
            getTopRatingSeriesUseCase,
            getPopularSeriesUseCase,
            getPopularMoviesUseCase,
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase,
            getAllMoviesUseCase,
            getAllSeriesUseCase
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        val section = state.sections[MediaContentType.TOP_RATING]
        assertThat(section?.movies).isEqualTo(topRatingMovies.map { it.toHomeMediaUiState() })
        assertThat(section?.series).isEqualTo(topRatingSeries.map { it.toHomeMediaUiState() })
    }

    @Test
    fun `should set error status when fetching popular media fails`() = runTest {
        // Given
        coEvery { getPopularMoviesUseCase.getPopularMovies(page = 1, categoryId = null) } throws NetworkException()
        coEvery { getPopularSeriesUseCase.getPopularSeries(page = 1, categoryId = null) } returns emptyList()

        // When
        viewModel = HomeViewModel(
            getFreeToWatchMoviesUseCase,
            getMoreRecommendedMoviesUseCase,
            getTopRatingMoviesUseCase,
            getTrendingMoviesUseCase,
            getUpcomingMoviesUseCase,
            getNowPlayingMoviesUseCase,
            getAiringTodaySeriesUseCase,
            getMoreRecommendedSeriesUseCase,
            getOnTvSeriesUseCase,
            getTopRatingSeriesUseCase,
            getPopularSeriesUseCase,
            getPopularMoviesUseCase,
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase,
            getAllMoviesUseCase,
            getAllSeriesUseCase
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(HomeScreenState.ScreenStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `should navigate to profile when onClickProfile is called`() = runTest {
        // When
        viewModel.effect.test {
            viewModel.onClickProfile()
            // Then
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateToProfile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to media details when onClickMedia is called`() = runTest {
        // When
        viewModel.effect.test {
            viewModel.onClickMedia(mediaId = 123, isMovie = true)
            // Then
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateMediaDetails(123, true))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to see all screen when onClickSeeAll is called`() = runTest {
        // When
        viewModel.effect.test {
            viewModel.onClickSeeAll(MediaContentType.TOP_RATING, MediaType.MOVIES)
            // Then
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateToSeeAllScreen(MediaContentType.TOP_RATING, MediaType.MOVIES))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should update selected tab and fetch media by category when CATEGORIES tab is selected`() = runTest {
        // Given
        val movies = listOf(movie1)
        val series = listOf(series1)
        coEvery { getAllMoviesUseCase.getAllMovies(page = 1, categoryId = null) } returns movies
        coEvery { getAllSeriesUseCase.getAllSeries(page = 1, categoryId = null) } returns series

        // When
        viewModel.onClickTab(HomeScreenState.Tab.CATEGORIES.ordinal)
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.selectedTab).isEqualTo(HomeScreenState.Tab.CATEGORIES)
        assertThat(state.categoriesMedia).isEqualTo(movies.map { it.toHomeMediaUiState() } + series.map { it.toHomeMediaUiState() })
    }

    @Test
    fun `should update selected genre and fetch media by category when genre is selected`() = runTest {
        // Given
        val genreId = 1L
        val movies = listOf(movie1)
        val series = listOf(series1)
        coEvery { getAllMoviesUseCase.getAllMovies(page = 1, categoryId = genreId.toString()) } returns movies
        coEvery { getAllSeriesUseCase.getAllSeries(page = 1, categoryId = genreId.toString()) } returns series
        viewModel.updateState {
            it.copy(genres = listOf(HomeScreenState.GenreUiState.defaultGenre, HomeScreenState.GenreUiState(genreId, "Action")))
        }

        // When
        viewModel.onGenreSelected(1)
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.selectedGenreIndex).isEqualTo(1)
        assertThat(state.categoriesMedia).isEqualTo(movies.map { it.toHomeMediaUiState() } + series.map { it.toHomeMediaUiState() })
    }

    @Test
    fun `should sort categories media by POPULARITY when sorting type is selected`() = runTest {
        // Given
        val genreId = 1L
        val movies = listOf(movie1)
        val series = listOf(series1)
        coEvery { getAllMoviesUseCase.getAllMovies(page = 1, categoryId = genreId.toString(), SortType.POPULAR) } returns movies
        coEvery { getAllSeriesUseCase.getAllSeries(page = 1, categoryId = genreId.toString(), SortType.POPULAR) } returns series
        viewModel.updateState {
            it.copy(
                genres = listOf(HomeScreenState.GenreUiState.defaultGenre, HomeScreenState.GenreUiState(genreId, "Action")),
                selectedGenreIndex = 1
            )
        }

        // When
        viewModel.onSortingSelected(HomeScreenState.SortingType.POPULARITY)
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.selectedSortingType).isEqualTo(HomeScreenState.SortingType.POPULARITY)
        assertThat(state.categoriesMedia).isEqualTo(movies.map { it.toHomeMediaUiState() } + series.map { it.toHomeMediaUiState() })
    }

    @Test
    fun `should handle error when fetching section data fails`() = runTest {
        // Given
        coEvery { getTopRatingMoviesUseCase.getTopRatingMovies(page = 1, categoryId = null) } throws NetworkException()
        coEvery { getTopRatingSeriesUseCase.getTopRatingSeries(page = 1, categoryId = null) } returns emptyList()

        // When
        viewModel = HomeViewModel(
            getFreeToWatchMoviesUseCase,
            getMoreRecommendedMoviesUseCase,
            getTopRatingMoviesUseCase,
            getTrendingMoviesUseCase,
            getUpcomingMoviesUseCase,
            getNowPlayingMoviesUseCase,
            getAiringTodaySeriesUseCase,
            getMoreRecommendedSeriesUseCase,
            getOnTvSeriesUseCase,
            getTopRatingSeriesUseCase,
            getPopularSeriesUseCase,
            getPopularMoviesUseCase,
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase,
            getAllMoviesUseCase,
            getAllSeriesUseCase
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(HomeScreenState.ScreenStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

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