package com.cairosquad.viewmodel.home

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Account
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
import kotlinx.coroutines.flow.flowOf
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
class HomeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var manageMoviesUseCase: ManageMoviesUseCase
    private lateinit var manageSeriesUseCase: ManageSeriesUseCase
    private lateinit var accountUseCase: AccountUseCase
    private lateinit var unifiedMediaPager: UnifiedMediaPager
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        Dispatchers.setMain(testDispatcher)
        manageMoviesUseCase = mockk(relaxed = true)
        manageSeriesUseCase = mockk(relaxed = true)
        accountUseCase = mockk(relaxed = true)
        unifiedMediaPager = mockk(relaxed = true)
        viewModel = HomeViewModel(
            manageMoviesUseCase = manageMoviesUseCase,
            manageSeriesUseCase = manageSeriesUseCase,
            accountUseCase = accountUseCase,
            unifiedMediaPager = unifiedMediaPager
        )
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            manageMoviesUseCase = manageMoviesUseCase,
            manageSeriesUseCase = manageSeriesUseCase,
            accountUseCase = accountUseCase,
            unifiedMediaPager = unifiedMediaPager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should initialize data when viewmodel created`() = runTest {
        coEvery { accountUseCase.getAccountDetails() } returns account
        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()

        viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.profileImage).isEqualTo("/avatar.jpg")
        assertThat(viewModel.screenState.value.popularMovies).isEqualTo(listOf(movie1.toUiState()))
        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.SUCCESS)
    }

    @Test
    fun `should set error status when loadHomeScreenData fails with InternetConnectionException`() =
        runTest {
            coEvery {
                manageMoviesUseCase.getPopularMovies(
                    1,
                    null
                )
            } throws InternetConnectionException()
            coEvery { manageMoviesUseCase.getTopRatingMovies(1) } throws InternetConnectionException()

            viewModel.loadHomeScreenData()
            advanceUntilIdle()

            assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
            assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.NO_INTERNET)
            assertThat(viewModel.screenState.value.isRefreshing).isFalse()
        }

    @Test
    fun `should set error status when loadHomeScreenData fails with NetworkException`() = runTest {
        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } throws NetworkException()
        coEvery { manageMoviesUseCase.getTopRatingMovies(1) } throws NetworkException()

        viewModel.loadHomeScreenData()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
    }

    @Test
    fun `should set error status when loadHomeScreenData fails with generic exception`() = runTest {
        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } throws IOException()
        coEvery { manageMoviesUseCase.getTopRatingMovies(1) } throws IOException()

        viewModel.loadHomeScreenData()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
    }

    @Test
    fun `should update profile image when loadHomeScreenData succeeds`() = runTest {
        coEvery { accountUseCase.getAccountDetails() } returns account
        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()

        viewModel.loadHomeScreenData()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.profileImage).isEqualTo("/avatar.jpg")
    }

    @Test
    fun `should not update profile image when loadHomeScreenData fails`() = runTest {
        coEvery { accountUseCase.getAccountDetails() } throws IOException()
        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()

        viewModel.loadHomeScreenData()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.profileImage).isEqualTo("")
    }

    @Test
    fun `should emit navigate to profile when onClickProfile called`() = runTest {
        viewModel.effect.test {
            viewModel.onProfileClick()
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateToProfile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit navigate to media details when onClickMedia called`() = runTest {
        viewModel.effect.test {
            viewModel.onMediaClick(123L, true)
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateMediaDetails(123L, true))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit navigate to see all screen when onClickSeeAll called`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllClick(MediaContentType.TOP_RATING, MediaType.MOVIES)
            assertThat(awaitItem()).isEqualTo(
                HomeEffect.NavigateToSeeAllScreen(
                    MediaContentType.TOP_RATING,
                    MediaType.MOVIES
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should update genre and fetch media when onGenreSelected called`() = runTest {
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id) } returns flowOf()
        viewModel.onTabClick(HomeScreenState.Tab.CATEGORIES.ordinal)
        advanceUntilIdle()

        viewModel.onGenreSelected(1)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedGenreIndex).isEqualTo(1)
    }

    @Test
    fun `should update tab and fetch media when onClickTab categories`() = runTest {
        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()

        viewModel.onTabClick(HomeScreenState.Tab.CATEGORIES.ordinal)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedTab).isEqualTo(HomeScreenState.Tab.CATEGORIES)
    }

    @Test
    fun `should update tab when onClickTab movies`() = runTest {
        viewModel.onTabClick(HomeScreenState.Tab.MOVIES.ordinal)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedTab).isEqualTo(HomeScreenState.Tab.MOVIES)
    }

    @Test
    fun `should update tab when onClickTab series`() = runTest {
        viewModel.onTabClick(HomeScreenState.Tab.TV_SHOWS.ordinal)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedTab).isEqualTo(HomeScreenState.Tab.TV_SHOWS)
    }

    @Test
    fun `should not update state when onSortingSelected same filter`() = runTest {
        viewModel.onSortingSelected(HomeScreenState.SortingType.ALL)
        advanceUntilIdle()
        val initialState = viewModel.screenState.value

        viewModel.onSortingSelected(HomeScreenState.SortingType.ALL)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value).isEqualTo(initialState)
    }

    @Test
    fun `should sort media by all when onSortingSelected all`() = runTest {
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id, null) } returns flowOf()

        viewModel.onTabClick(HomeScreenState.Tab.CATEGORIES.ordinal)
        viewModel.onGenreSelected(1)
        viewModel.onSortingSelected(HomeScreenState.SortingType.ALL)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedSortingType).isEqualTo(HomeScreenState.SortingType.ALL)
    }

    @Test
    fun `should sort media by all when onSortingSelected popularity`() = runTest {
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id, null) } returns flowOf()

        viewModel.onTabClick(HomeScreenState.Tab.CATEGORIES.ordinal)
        viewModel.onGenreSelected(1)
        viewModel.onSortingSelected(HomeScreenState.SortingType.POPULARITY)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedSortingType).isEqualTo(HomeScreenState.SortingType.POPULARITY)
    }

    @Test
    fun `should sort media by all when onSortingSelected latest`() = runTest {
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id, null) } returns flowOf()

        viewModel.onTabClick(2)
        viewModel.onGenreSelected(1)
        viewModel.onSortingSelected(HomeScreenState.SortingType.LATEST)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedSortingType).isEqualTo(HomeScreenState.SortingType.LATEST)
    }

    @Test
    fun `should reload all data when onRefresh called in Movies Tab`() = runTest {
        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getTopRatingMovies(1) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getNowPlayingMovies(1) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getUpcomingMovies(1) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getMoreRecommendedMovies(1) } returns listOf(movie1)

        viewModel.onTabClick(HomeScreenState.Tab.MOVIES.ordinal)
        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.popularMovies).isEqualTo(listOf(movie1.toUiState()))
        assertThat(viewModel.screenState.value.movieSections.topRating).isEqualTo(listOf(movie1.toUiState()))
        assertThat(viewModel.screenState.value.movieSections.nowPlaying).isEqualTo(listOf(movie1.toUiState()))
        assertThat(viewModel.screenState.value.movieSections.upComing).isEqualTo(listOf(movie1.toUiState()))
        assertThat(viewModel.screenState.value.movieSections.moreRecommended).isEqualTo(
            listOf(
                movie1.toUiState()
            )
        )
    }

    @Test
    fun `should reload all data when onRefresh called in Series Tab`() = runTest {
        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
        coEvery { manageSeriesUseCase.getTopRatingSeries(1) } returns listOf(series1)
        coEvery { manageSeriesUseCase.getAiringTodaySeries(1) } returns listOf(series1)
        coEvery { manageSeriesUseCase.getOnTvSeries(1) } returns listOf(series1)
        coEvery { manageSeriesUseCase.getMoreRecommendedSeries(1) } returns listOf(series1)

        viewModel.onTabClick(HomeScreenState.Tab.TV_SHOWS.ordinal)
        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.popularSeries).isEqualTo(listOf(series1.toUiState()))
        assertThat(viewModel.screenState.value.seriesSections.topRating).isEqualTo(listOf(series1.toUiState()))
        assertThat(viewModel.screenState.value.seriesSections.onTv).isEqualTo(listOf(series1.toUiState()))
        assertThat(viewModel.screenState.value.seriesSections.airingToday).isEqualTo(listOf(series1.toUiState()))
        assertThat(viewModel.screenState.value.seriesSections.moreRecommended).isEqualTo(
            listOf(
                series1.toUiState()
            )
        )
    }

    @Test
    fun `should reload all data when onRefresh called in Categories Tab`() = runTest {
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()

        viewModel.onTabClick(HomeScreenState.Tab.CATEGORIES.ordinal)
        viewModel.onRefresh()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.genres).isEqualTo(
            listOf(
                HomeScreenState.GenreUiState.defaultGenre,
                genre1.toUiState(),
                genre2.toUiState()
            )
        )
    }

    private companion object {
        val account = Account(
            id = 1L,
            name = "John Doe",
            username = "johndoe",
            avatarPath = "/avatar.jpg"
        )

        val genre1 = Genre(id = 1, name = "Action")
        val genre2 = Genre(id = 2, name = "Drama")

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

        val movie2 = Movie(
            id = 2,
            title = "Inception",
            rating = 4.5f,
            posterPath = "/img2.jpg",
            genres = listOf(genre2),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )

        val series1 = Series(
            id = 1,
            title = "Breaking Bad",
            rating = 4.8f,
            posterPath = "/series1.jpg",
            trailerPath = "",
            genres = listOf(genre1),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 5
        )

        val series2 = Series(
            id = 2,
            title = "Stranger Things",
            rating = 4.3f,
            posterPath = "/series2.jpg",
            trailerPath = "",
            genres = listOf(genre2),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 4
        )
    }
}