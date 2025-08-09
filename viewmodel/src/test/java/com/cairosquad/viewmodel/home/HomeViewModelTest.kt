package com.cairosquad.viewmodel.home

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Account
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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

//    @Test
//    fun `should initialize data when viewmodel created`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//        viewModel = HomeViewModel(
//            manageMoviesUseCase,
//            manageSeriesUseCase,
//            accountUseCase,
//            unifiedMediaPager
//        )
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.profileImage).isEqualTo("/avatar.jpg")
//        assertThat(viewModel.screenState.value.popularMovies).isEqualTo(listOf(movie1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.popularSeries).isEqualTo(listOf(series1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.genres).isEqualTo(
//            listOf(
//                HomeScreenState.GenreUiState.defaultGenre,
//                genre1.toHomeGenreUiState(),
//                genre2.toHomeGenreUiState()
//            )
//        )
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.SUCCESS)
//    }

//    @Test
//    fun `should load all data when loadHomeScreenData called`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//        viewModel.loadHomeScreenData()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.profileImage).isEqualTo("/avatar.jpg")
//        assertThat(viewModel.screenState.value.popularMovies).isEqualTo(listOf(movie1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.popularSeries).isEqualTo(listOf(series1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.genres).isEqualTo(
//            listOf(
//                HomeScreenState.GenreUiState.defaultGenre,
//                genre1.toHomeGenreUiState(),
//                genre2.toHomeGenreUiState()
//            )
//        )
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.SUCCESS)
//    }
//
//    @Test
//    fun `should update popular media when loadHomeScreenData succeeds`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//        viewModel.loadHomeScreenData()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.popularMovies).isEqualTo(listOf(movie1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.popularSeries).isEqualTo(listOf(series1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.SUCCESS)
//        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
//    }
//
//    @Test
//    fun `should set error status when loadHomeScreenData fails with InternetConnectionException`() =
//        runTest {
//            coEvery { accountUseCase.getAccountDetails() } returns account
//            coEvery {
//                manageMoviesUseCase.getPopularMovies(
//                    1,
//                    null
//                )
//            } throws InternetConnectionException()
//            coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//            coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//            coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//            viewModel.loadHomeScreenData()
//            advanceUntilIdle()
//
//            assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
//            assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.NO_INTERNET)
//            assertThat(viewModel.screenState.value.isRefreshing).isFalse()
//        }
//
//    @Test
//    fun `should set error status when loadHomeScreenData fails with NetworkException`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } throws NetworkException()
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//        viewModel.loadHomeScreenData()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
//        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
//        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
//    }
//
//    @Test
//    fun `should set error status when loadHomeScreenData fails with generic exception`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } throws IOException()
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//        viewModel.loadHomeScreenData()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
//        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
//        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
//    }
//
//    @Test
//    fun `should load genres when loadHomeScreenData succeeds`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()
//
//        viewModel.loadHomeScreenData()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.genres).isEqualTo(
//            listOf(
//                HomeScreenState.GenreUiState.defaultGenre,
//                genre1.toHomeGenreUiState(),
//                genre2.toHomeGenreUiState()
//            )
//        )
//    }


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
            viewModel.onClickProfile()
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateToProfile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit navigate to media details when onClickMedia called`() = runTest {
        viewModel.effect.test {
            viewModel.onClickMedia(123L, true)
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateMediaDetails(123L, true))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit navigate to see all screen when onClickSeeAll called`() = runTest {
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
//    fun `should update genre and fetch media when onGenreSelected called`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
//        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id) } returns flowOf()
//        viewModel.loadHomeScreenData()
//        advanceUntilIdle()
//
//        viewModel.onGenreSelected(1)
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.selectedGenreIndex).isEqualTo(1)
//    }

    @Test
    fun `should update tab and fetch media when onClickTab categories`() = runTest {
        coEvery { unifiedMediaPager.getCombinedMedia(null) } returns flowOf()

        viewModel.onClickTab(HomeScreenState.Tab.CATEGORIES.ordinal)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedTab).isEqualTo(HomeScreenState.Tab.CATEGORIES)
    }

    @Test
    fun `should update tab when onClickTab movies`() = runTest {
        viewModel.onClickTab(HomeScreenState.Tab.MOVIES.ordinal)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.selectedTab).isEqualTo(HomeScreenState.Tab.MOVIES)
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

//    @Test
//    fun `should sort media by all when onSortingSelected all`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
//        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id, null) } returns flowOf()
//        viewModel.loadHomeScreenData()
//        viewModel.onGenreSelected(1)
//        advanceUntilIdle()
//
//        viewModel.onSortingSelected(HomeScreenState.SortingType.ALL)
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.selectedSortingType).isEqualTo(HomeScreenState.SortingType.ALL)
//    }
//
//    @Test
//    fun `should sort media by popularity when onSortingSelected popularity`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
//        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id, SortType.POPULAR) } returns flowOf()
//        viewModel.loadHomeScreenData()
//        viewModel.onGenreSelected(1)
//        advanceUntilIdle()
//
//        viewModel.onSortingSelected(HomeScreenState.SortingType.POPULARITY)
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.selectedSortingType).isEqualTo(HomeScreenState.SortingType.POPULARITY)
//    }
//
//    @Test
//    fun `should sort media by latest when onSortingSelected latest`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns emptyList()
//        coEvery { unifiedMediaPager.getCombinedMedia(genre1.id, SortType.LATEST) } returns flowOf()
//        viewModel.loadHomeScreenData()
//        viewModel.onGenreSelected(1)
//        advanceUntilIdle()
//
//        viewModel.onSortingSelected(HomeScreenState.SortingType.LATEST)
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.selectedSortingType).isEqualTo(HomeScreenState.SortingType.LATEST)
//    }
//
//
//    @Test
//    fun `should fetch top rating data when onSectionVisible top rating`() = runTest {
//        coEvery { manageMoviesUseCase.getTopRatingMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getTopRatingSeries(1, null) } returns listOf(series1)
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.TOP_RATING to HomeScreenState.SectionUiState(
//                    movies = listOf(movie1.toHomeMediaUiState()),
//                    series = listOf(series1.toHomeMediaUiState()),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }
//
//    @Test
//    fun `should fetch now playing data when onSectionVisible now playing`() = runTest {
//        coEvery { manageMoviesUseCase.getNowPlayingMovies(1, null) } returns listOf(movie1)
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.NOW_PLAYING to HomeScreenState.SectionUiState(
//                    movies = listOf(movie1.toHomeMediaUiState()),
//                    series = emptyList(),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }
//
//    @Test
//    fun `should fetch upcoming data when onSectionVisible upcoming`() = runTest {
//        coEvery { manageMoviesUseCase.getUpcomingMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getFreeToWatchSeries(1, null) } returns listOf(series1)
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.UPCOMING to HomeScreenState.SectionUiState(
//                    movies = listOf(movie1.toHomeMediaUiState()),
//                    series = listOf(series1.toHomeMediaUiState()),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }
//
//    @Test
//    fun `should fetch more recommended data when onSectionVisible more recommended`() = runTest {
//        coEvery { manageMoviesUseCase.getMoreRecommendedMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getMoreRecommendedSeries(1, null) } returns listOf(series1)
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.MORE_RECOMMENDED to HomeScreenState.SectionUiState(
//                    movies = listOf(movie1.toHomeMediaUiState()),
//                    series = listOf(series1.toHomeMediaUiState()),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }
//
//    @Test
//    fun `should fetch free to watch data when onSectionVisible free to watch`() = runTest {
//        coEvery { manageMoviesUseCase.getFreeToWatchMovies(1, null) } returns listOf(movie1)
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.FREE_TO_WATCH to HomeScreenState.SectionUiState(
//                    movies = listOf(movie1.toHomeMediaUiState()),
//                    series = emptyList(),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }
//
//    @Test
//    fun `should fetch airing today data when onSectionVisible airing today`() = runTest {
//        coEvery { manageSeriesUseCase.getAiringTodaySeries(1, null) } returns listOf(series1)
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.AIRING_TODAY to HomeScreenState.SectionUiState(
//                    movies = emptyList(),
//                    series = listOf(series1.toHomeMediaUiState()),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }

//    @Test
//    fun `should fetch on tv data when onSectionVisible on tv`() = runTest {
//        coEvery { manageSeriesUseCase.getOnTvSeries(1, null) } returns listOf(series1)
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.ON_TV to HomeScreenState.SectionUiState(
//                    movies = emptyList(),
//                    series = listOf(series1.toHomeMediaUiState()),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//    }

//    @Test
//    fun `should set error status when onSectionVisible fails with exception`() = runTest {
//        coEvery { manageMoviesUseCase.getTopRatingMovies(1, null) } throws IOException()
//
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.FAILED)
//        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
//    }
//
//    @Test
//    fun `should reload all data when onRefresh called`() = runTest {
//        coEvery { accountUseCase.getAccountDetails() } returns account
//        coEvery { manageMoviesUseCase.getPopularMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getPopularSeries(1, null) } returns listOf(series1)
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(genre1)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(genre2)
//        coEvery { manageMoviesUseCase.getTopRatingMovies(1, null) } returns listOf(movie1)
//        coEvery { manageSeriesUseCase.getTopRatingSeries(1, null) } returns listOf(series1)
//
//        viewModel.onRefresh()
//        advanceUntilIdle()
//
//        assertThat(viewModel.screenState.value.profileImage).isEqualTo("/avatar.jpg")
//        assertThat(viewModel.screenState.value.popularMovies).isEqualTo(listOf(movie1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.popularSeries).isEqualTo(listOf(series1.toHomeMediaUiState()))
//        assertThat(viewModel.screenState.value.genres).isEqualTo(
//            listOf(
//                HomeScreenState.GenreUiState.defaultGenre,
//                genre1.toHomeGenreUiState(),
//                genre2.toHomeGenreUiState()
//            )
//        )
//        assertThat(viewModel.screenState.value.sections).isEqualTo(
//            mapOf(
//                MediaContentType.TOP_RATING to HomeScreenState.SectionUiState(
//                    movies = listOf(movie1.toHomeMediaUiState()),
//                    series = listOf(series1.toHomeMediaUiState()),
//                    isLoading = false
//                )
//            ).entries.sortedBy { it.key.ordinal }.associate { it.key to it.value }
//        )
//        assertThat(viewModel.screenState.value.dataRequestStatus).isEqualTo(HomeScreenState.DataRequestStatus.SUCCESS)
//        assertThat(viewModel.screenState.value.isRefreshing).isFalse()
//    }

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