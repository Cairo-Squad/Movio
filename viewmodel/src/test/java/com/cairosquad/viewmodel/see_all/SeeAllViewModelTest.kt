//package com.cairosquad.viewmodel.see_all
//
//import androidx.paging.PagingData
//import app.cash.turbine.test
//import com.cairosquad.domain.exception.DUnauthorizedException
//import com.cairosquad.domain.exception.DomainEmptyResponseException
//import com.cairosquad.domain.exception.DomainJsonParsingException
//import com.cairosquad.domain.exception.InternetConnectionException
//import com.cairosquad.domain.exception.MovioException
//import com.cairosquad.domain.exception.NetworkException
//import com.cairosquad.domain.exception.UnknownException
//import com.cairosquad.domain.usecase.ManageMoviesUseCase
//import com.cairosquad.domain.usecase.ManageSeriesUseCase
//import com.cairosquad.entity.Genre
//import com.cairosquad.entity.Movie
//import com.cairosquad.entity.Series
//import com.cairosquad.viewmodel.exception.ErrorStatus
//import com.cairosquad.viewmodel.util.MediaContentType
//import com.cairosquad.viewmodel.util.MediaType
//import io.mockk.coEvery
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.mockkStatic
//import io.mockk.verify
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import kotlin.test.assertFalse
//import kotlin.test.assertNotNull
//import kotlin.test.assertNull
//import kotlin.test.assertTrue
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class SeeAllViewModelTest {
//
//    private lateinit var viewModel: SeeAllViewModel
//
//    private val testDispatcher = StandardTestDispatcher()
//    private val manageMoviesUseCase = mockk<ManageMoviesUseCase>()
//    private val manageSeriesUseCase = mockk<ManageSeriesUseCase>()
//    private val seeAllMoviesPager = mockk<SeeAllMoviesPager>()
//    private val seeAllSeriesPager = mockk<SeeAllSeriesPager>()
//
//    private val dummyMovie = Movie(
//        1, "Title", 8.5f, "path.jpg", listOf(), "overview", 1234567890, 120, "trailer"
//    )
//    private val dummyMovie2 = Movie(
//        2, "Title 2", 7.8f, "path2.jpg", listOf(), "overview 2", 1234567890, 110, "trailer2"
//    )
//    private val dummySeries = Series(
//        1, "Series Title", 8.2f, "series_path.jpg", listOf(), "series overview", 1234567890, listOf(45), "series_trailer"
//    )
//    private val dummyGenre = Genre(1, "Action")
//    private val dummyGenre2 = Genre(2, "Comedy")
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//
//        mockkStatic(Dispatchers::class)
//        every { Dispatchers.IO } returns testDispatcher
//
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(dummyGenre)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//        every { seeAllSeriesPager.getTopRatingSeries(any()) } returns flowOf(PagingData.from(listOf(dummySeries)))
//
//        viewModel = SeeAllViewModel(
//            manageMoviesUseCase,
//            manageSeriesUseCase,
//            seeAllMoviesPager,
//            seeAllSeriesPager
//        )
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `initial state is correct`() {
//        val initialState = viewModel.screenState.value
//        assertEquals(SeeAllScreenState.ScreenStatus.LOADING, initialState.screenStatus)
//        assertEquals(1, initialState.genres.size) // Default "All" genre
//        assertEquals(0, initialState.selectedGenreIndex)
//        assertNull(initialState.errorStatus)
//        assertFalse(initialState.isRefreshing)
//    }
//
//    @Test
//    fun `onClickBack sends NavigateBack effect`() = runTest(testDispatcher) {
//        viewModel.effect.test {
//            viewModel.onClickBack()
//            assertEquals(SeeAllEffect.NavigateBack, awaitItem())
//            expectNoEvents()
//        }
//    }
//
//    @Test
//    fun `onClickMedia with movie sends correct NavigateMediaDetails effect`() = runTest(testDispatcher) {
//        viewModel.effect.test {
//            viewModel.onClickMedia(123, true)
//            val effect = awaitItem()
//            assertEquals(SeeAllEffect.NavigateMediaDetails(123, true), effect)
//            expectNoEvents()
//        }
//    }
//
//    @Test
//    fun `onClickMedia with series sends correct NavigateMediaDetails effect`() = runTest(testDispatcher) {
//        viewModel.effect.test {
//            viewModel.onClickMedia(456, false)
//            val effect = awaitItem()
//            assertEquals(SeeAllEffect.NavigateMediaDetails(456, false), effect)
//            expectNoEvents()
//        }
//    }
//
//    @Test
//    fun `multiple onClickMedia calls send multiple effects`() = runTest(testDispatcher) {
//        viewModel.effect.test {
//            viewModel.onClickMedia(1, true)
//            viewModel.onClickMedia(2, false)
//            viewModel.onClickMedia(3, true)
//
//            assertEquals(SeeAllEffect.NavigateMediaDetails(1, true), awaitItem())
//            assertEquals(SeeAllEffect.NavigateMediaDetails(2, false), awaitItem())
//            assertEquals(SeeAllEffect.NavigateMediaDetails(3, true), awaitItem())
//            expectNoEvents()
//        }
//    }
//
//    @Test
//    fun `updateScreenStatus should update screen status correctly`() {
//        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.LOADING)
//        assertEquals(SeeAllScreenState.ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
//
//        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.SUCCESS)
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//
//        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.FAILED)
//        assertEquals(SeeAllScreenState.ScreenStatus.FAILED, viewModel.screenState.value.screenStatus)
//    }
//
//    @Test
//    fun `updateErrorStatus should update error status correctly`() {
//        viewModel.updateErrorStatus(ErrorStatus.NETWORK_ERROR)
//        assertEquals(ErrorStatus.NETWORK_ERROR, viewModel.screenState.value.errorStatus)
//
//        viewModel.updateErrorStatus(ErrorStatus.NO_INTERNET)
//        assertEquals(ErrorStatus.NO_INTERNET, viewModel.screenState.value.errorStatus)
//
//        viewModel.updateErrorStatus(null)
//        assertNull(viewModel.screenState.value.errorStatus)
//    }
//
//    @Test
//    fun `onRefresh should toggle isRefreshing and reload data`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre, dummyGenre2)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        viewModel.onRefresh(1)
//
//        assertTrue(viewModel.screenState.value.isRefreshing)
//
//        advanceUntilIdle()
//
//        assertFalse(viewModel.screenState.value.isRefreshing)
//        verify { seeAllMoviesPager.getTopRatingMovies(dummyGenre.id) }
//    }
//
//    @Test
//    fun `onGenreSelected updates selectedGenreIndex and reloads data`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre, dummyGenre2)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.mediaType = MediaType.MOVIES
//        viewModel.contentType = MediaContentType.TOP_RATING
//        viewModel.loadGenres()
//        advanceUntilIdle()
//
//        viewModel.onGenreSelected(1)
//
//        assertEquals(1, viewModel.screenState.value.selectedGenreIndex)
//        verify { seeAllMoviesPager.getTopRatingMovies(dummyGenre.id) }
//    }
//
//    @Test
//    fun `onGenreSelected should ignore same index selection`() = runTest {
//        val initialIndex = viewModel.screenState.value.selectedGenreIndex
//
//        viewModel.onGenreSelected(initialIndex)
//
//        assertEquals(initialIndex, viewModel.screenState.value.selectedGenreIndex)
//    }
//
//    @Test
//    fun `loadData should load movies successfully`() = runTest {
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//        assertEquals(MediaType.MOVIES, viewModel.mediaType)
//        assertEquals(MediaContentType.TOP_RATING, viewModel.contentType)
//        assertNotNull(viewModel.screenState.value.mediaList)
//        assertTrue(viewModel.screenState.value.genres.isNotEmpty())
//    }
//
//    @Test
//    fun `loadData should load series successfully`() = runTest {
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.SERIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//        assertEquals(MediaType.SERIES, viewModel.mediaType)
//        assertEquals(MediaContentType.TOP_RATING, viewModel.contentType)
//        assertNotNull(viewModel.screenState.value.mediaList)
//        assertTrue(viewModel.screenState.value.genres.isNotEmpty())
//    }
//
//    @Test
//    fun `loadData should set loading status initially`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//
//        assertEquals(SeeAllScreenState.ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
//        assertNull(viewModel.screenState.value.errorStatus)
//
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//    }
//
//    @Test
//    fun `loadData should handle trending movies`() = runTest {
//        every { seeAllMoviesPager.getTrendingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadData(MediaContentType.TRENDING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//        verify { seeAllMoviesPager.getTrendingMovies(null) }
//    }
//
//    @Test
//    fun `loadData should handle upcoming movies only`() = runTest {
//        every { seeAllMoviesPager.getUpcomingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadData(MediaContentType.UPCOMING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//        verify { seeAllMoviesPager.getUpcomingMovies(null) }
//    }
//
//    @Test
//    fun `loadData should handle airing today series only`() = runTest {
//        every { seeAllSeriesPager.getAiringTodaySeries(any()) } returns flowOf(PagingData.from(listOf(dummySeries)))
//
//        viewModel.loadData(MediaContentType.AIRING_TODAY, MediaType.SERIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//        verify { seeAllSeriesPager.getAiringTodaySeries(null) }
//    }
//
//    @Test
//    fun `loadData should handle error and set failed status`() = runTest {
//        val exception = NetworkException()
//        coEvery { manageMoviesUseCase.getMoviesGenres() } throws exception
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.FAILED, viewModel.screenState.value.screenStatus)
//        assertEquals(ErrorStatus.NETWORK_ERROR, viewModel.screenState.value.errorStatus)
//    }
//
//    @Test
//    fun `loadData should handle paging data transformation correctly`() = runTest {
//        val movies = listOf(dummyMovie, dummyMovie2)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(movies))
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        val mediaList = viewModel.screenState.value.mediaList
//        assertNotNull(mediaList)
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//    }
//
//    @Test
//    fun `getDataPagerFetcher2 should return correct pager functions for each content type`() {
//        val (moviesFetcher, seriesFetcher) = viewModel.getDataPagerFetcher2(MediaContentType.TRENDING, null)
//
//        moviesFetcher()
//        seriesFetcher()
//
//        verify { seeAllMoviesPager.getTrendingMovies(null) }
//        verify { seeAllSeriesPager.getTrendingSeries(null) }
//    }
//
//    @Test
//    fun `getDataPagerFetcher2 should return correct flows for all content types`() {
//        MediaContentType.entries.forEach { contentType ->
//            val (movieFlow, seriesFlow) = viewModel.getDataPagerFetcher2(contentType, null)
//
//            assertNotNull(movieFlow)
//            assertNotNull(seriesFlow)
//
//            movieFlow()
//            seriesFlow()
//        }
//    }
//
//    @Test
//    fun `getDataPagerFetcher2 should handle free to watch content`() {
//        every { seeAllMoviesPager.getFreeToWatchMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//        every { seeAllSeriesPager.getFreeToWatchSeries(any()) } returns flowOf(PagingData.from(listOf(dummySeries)))
//
//        val (moviesFetcher, seriesFetcher) = viewModel.getDataPagerFetcher2(MediaContentType.FREE_TO_WATCH, null)
//
//        moviesFetcher()
//        seriesFetcher()
//
//        verify { seeAllMoviesPager.getFreeToWatchMovies(null) }
//        verify { seeAllSeriesPager.getFreeToWatchSeries(null) }
//    }
//
//    @Test
//    fun `getDataPagerFetcher2 should return empty flow for unsupported combinations`() {
//        val (moviesFetcher, seriesFetcher) = viewModel.getDataPagerFetcher2(MediaContentType.UPCOMING, null)
//
//        moviesFetcher()
//        seriesFetcher()
//
//        verify { seeAllMoviesPager.getUpcomingMovies(null) }
//    }
//
//    @Test
//    fun `getDataFetcher returns correct use cases for TRENDING`() {
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.TRENDING)
//        assertNotNull(movieFetcher)
//        assertNotNull(seriesFetcher)
//    }
//
//    @Test
//    fun `getDataFetcher for TOP_RATING returns correct use cases`() = runTest {
//        val testPage = 1
//        val testGenreId = 123L
//        val testMovies = listOf(mockk<Movie>())
//        val testSeries = listOf(mockk<Series>())
//
//        coEvery {
//            manageMoviesUseCase.getTopRatingMovies(testPage, testGenreId)
//        } returns testMovies
//        coEvery {
//            manageSeriesUseCase.getTopRatingSeries(testPage, testGenreId)
//        } returns testSeries
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.TOP_RATING)
//
//        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
//        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
//    }
//
//    @Test
//    fun `getDataFetcher for TRENDING returns correct use cases`() = runTest {
//        val testPage = 2
//        val testGenreId = 456L
//        val testMovies = listOf(mockk<Movie>())
//        val testSeries = listOf(mockk<Series>())
//
//        coEvery {
//            manageMoviesUseCase.getTrendingMovies(testPage, testGenreId)
//        } returns testMovies
//        coEvery {
//            manageSeriesUseCase.getTrendingSeries(testPage, testGenreId)
//        } returns testSeries
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.TRENDING)
//
//        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
//        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
//    }
//
//    @Test
//    fun `getDataFetcher for FREE_TO_WATCH returns movies use case and empty series`() = runTest {
//        val testPage = 3
//        val testGenreId = 789L
//        val testMovies = listOf(mockk<Movie>())
//
//        coEvery {
//            manageMoviesUseCase.getFreeToWatchMovies(testPage, testGenreId)
//        } returns testMovies
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.FREE_TO_WATCH)
//
//        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
//        assertTrue(seriesFetcher(testPage, testGenreId).isEmpty())
//    }
//
//    @Test
//    fun `getDataFetcher for UPCOMING returns movies use case and empty series`() = runTest {
//        val testPage = 1
//        val testGenreId = 123L
//        val testMovies = listOf(mockk<Movie>())
//
//        coEvery {
//            manageMoviesUseCase.getUpcomingMovies(testPage, testGenreId)
//        } returns testMovies
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.UPCOMING)
//
//        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
//        assertTrue(seriesFetcher(testPage, testGenreId).isEmpty())
//    }
//
//    @Test
//    fun `getDataFetcher for NOW_PLAYING returns movies use case and empty series`() = runTest {
//        val testPage = 2
//        val testGenreId = 456L
//        val testMovies = listOf(mockk<Movie>())
//
//        coEvery {
//            manageMoviesUseCase.getNowPlayingMovies(testPage, testGenreId)
//        } returns testMovies
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.NOW_PLAYING)
//
//        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
//        assertTrue(seriesFetcher(testPage, testGenreId).isEmpty())
//    }
//
//    @Test
//    fun `getDataFetcher for MORE_RECOMMENDED returns both movies and series use cases`() = runTest {
//        val testPage = 3
//        val testGenreId = 789L
//        val testMovies = listOf(mockk<Movie>())
//        val testSeries = listOf(mockk<Series>())
//
//        coEvery {
//            manageMoviesUseCase.getMoreRecommendedMovies(testPage, testGenreId)
//        } returns testMovies
//        coEvery {
//            manageSeriesUseCase.getMoreRecommendedSeries(testPage, testGenreId)
//        } returns testSeries
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.MORE_RECOMMENDED)
//
//        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
//        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
//    }
//
//    @Test
//    fun `getDataFetcher for AIRING_TODAY returns empty movies and series use case`() = runTest {
//        val testPage = 4
//        val testGenreId = 101L
//        val testSeries = listOf(mockk<Series>())
//
//        coEvery {
//            manageSeriesUseCase.getAiringTodaySeries(testPage, testGenreId)
//        } returns testSeries
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.AIRING_TODAY)
//
//        assertTrue(movieFetcher(testPage, testGenreId).isEmpty())
//        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
//    }
//
//    @Test
//    fun `getDataFetcher for ON_TV returns empty movies and series use case`() = runTest {
//        val testPage = 5
//        val testGenreId = 112L
//        val testSeries = listOf(mockk<Series>())
//
//        coEvery {
//            manageSeriesUseCase.getOnTvSeries(testPage, testGenreId)
//        } returns testSeries
//
//        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.ON_TV)
//
//        assertTrue(movieFetcher(testPage, testGenreId).isEmpty())
//        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
//    }
//
//    @Test
//    fun `getDataFetcher for all MediaContentTypes returns non-null functions`() {
//        MediaContentType.entries.forEach { type ->
//            val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(type)
//            assertNotNull(movieFetcher)
//            assertNotNull(seriesFetcher)
//        }
//    }
//
//    @Test
//    fun `getDataFetcher handles null genreId correctly`() = runTest {
//        val testPage = 1
//        val testMovies = listOf(mockk<Movie>())
//
//        coEvery { manageMoviesUseCase.getTopRatingMovies(testPage, null) } returns testMovies
//
//        val (movieFetcher, _) = viewModel.getDataFetcher(MediaContentType.TOP_RATING)
//
//        assertEquals(testMovies, movieFetcher(testPage, null))
//    }
//
//    @Test
//    fun `getDataFetcher handles null genreId correctly for all types`() = runTest {
//        listOf(
//            MediaContentType.UPCOMING,
//            MediaContentType.NOW_PLAYING,
//            MediaContentType.MORE_RECOMMENDED
//        ).forEach { type ->
//            val testPage = 1
//            val testMovies = listOf(mockk<Movie>())
//
//            when (type) {
//                MediaContentType.UPCOMING -> coEvery {
//                    manageMoviesUseCase.getUpcomingMovies(testPage, null)
//                } returns testMovies
//
//                MediaContentType.NOW_PLAYING -> coEvery {
//                    manageMoviesUseCase.getNowPlayingMovies(testPage, null)
//                } returns testMovies
//
//                MediaContentType.MORE_RECOMMENDED -> coEvery {
//                    manageMoviesUseCase.getMoreRecommendedMovies(testPage, null)
//                } returns testMovies
//
//                else -> {}
//            }
//
//            val (movieFetcher, _) = viewModel.getDataFetcher(type)
//            assertEquals(testMovies, movieFetcher(testPage, null))
//        }
//
//        listOf(
//            MediaContentType.MORE_RECOMMENDED,
//            MediaContentType.AIRING_TODAY,
//            MediaContentType.ON_TV
//        ).forEach { type ->
//            val testPage = 1
//            val testSeries = listOf(mockk<Series>())
//
//            when (type) {
//                MediaContentType.MORE_RECOMMENDED -> coEvery {
//                    manageSeriesUseCase.getMoreRecommendedSeries(testPage, null)
//                } returns testSeries
//
//                MediaContentType.AIRING_TODAY -> coEvery {
//                    manageSeriesUseCase.getAiringTodaySeries(testPage, null)
//                } returns testSeries
//
//                MediaContentType.ON_TV -> coEvery {
//                    manageSeriesUseCase.getOnTvSeries(testPage, null)
//                } returns testSeries
//
//                else -> {}
//            }
//
//            val (_, seriesFetcher) = viewModel.getDataFetcher(type)
//            assertEquals(testSeries, seriesFetcher(testPage, null))
//        }
//    }
//
//    @Test
//    fun `loadGenres with MOVIES updates genre list`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre, dummyGenre2)
//
//        viewModel.mediaType = MediaType.MOVIES
//        viewModel.loadGenres()
//        advanceUntilIdle()
//
//        val genres = viewModel.screenState.value.genres
//        assertTrue(genres.size >= 2) // At least default "All" + movie genres
//        assertTrue(genres.any { it.name == "All" })
//    }
//
//    @Test
//    fun `loadGenres with SERIES updates genre list`() = runTest {
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(dummyGenre)
//
//        viewModel.mediaType = MediaType.SERIES
//        viewModel.loadGenres()
//        advanceUntilIdle()
//
//        val genres = viewModel.screenState.value.genres
//        assertTrue(genres.size >= 1)
//        assertTrue(genres.any { it.name == "All" })
//    }
//
//    @Test
//    fun `loadGenres should handle empty genre list`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns emptyList()
//
//        viewModel.mediaType = MediaType.MOVIES
//        viewModel.loadGenres()
//        advanceUntilIdle()
//
//        assertEquals(1, viewModel.screenState.value.genres.size)
//        assertEquals("All", viewModel.screenState.value.genres[0].name)
//    }
//
//    @Test
//    fun `loadGenres should handle error`() = runTest {
//        val exception = InternetConnectionException()
//        coEvery { manageMoviesUseCase.getMoviesGenres() } throws exception
//
//        viewModel.mediaType = MediaType.MOVIES
//        viewModel.loadGenres()
//        advanceUntilIdle()
//
//        assertEquals(ErrorStatus.NO_INTERNET, viewModel.screenState.value.errorStatus)
//    }
//
//    @Test
//    fun `handleError maps exceptions to ErrorStatus correctly`() = runTest {
//        val exceptionMapping = mapOf<MovioException, ErrorStatus>(
//            NetworkException() to ErrorStatus.NETWORK_ERROR,
//            InternetConnectionException() to ErrorStatus.NO_INTERNET,
//            UnknownException() to ErrorStatus.UNKNOWN_ERROR,
//            DUnauthorizedException() to ErrorStatus.UNAUTHORIZED,
//            DomainEmptyResponseException() to ErrorStatus.EMPTY,
//            DomainJsonParsingException() to ErrorStatus.PARSING_ERROR
//        )
//
//        exceptionMapping.forEach { (exception, expectedStatus) ->
//            viewModel.handleError(exception)
//
//            val state = viewModel.screenState.value
//            assertEquals(SeeAllScreenState.ScreenStatus.FAILED, state.screenStatus)
//            assertEquals(expectedStatus, state.errorStatus)
//
//            viewModel.updateState { SeeAllScreenState() }
//        }
//    }
//
//    @Test
//    fun `handleError maps exceptions to correct ErrorStatus`() = runTest {
//        val testCases = mapOf(
//            NetworkException() to ErrorStatus.NETWORK_ERROR,
//            InternetConnectionException() to ErrorStatus.NO_INTERNET,
//            UnknownException() to ErrorStatus.UNKNOWN_ERROR,
//            DUnauthorizedException() to ErrorStatus.UNAUTHORIZED,
//            DomainEmptyResponseException() to ErrorStatus.EMPTY,
//            DomainJsonParsingException() to ErrorStatus.PARSING_ERROR,
//            Exception() to ErrorStatus.UNKNOWN_ERROR
//        )
//
//        testCases.forEach { (exception, expectedStatus) ->
//            viewModel.handleError(exception)
//
//            val state = viewModel.screenState.value
//            assertEquals(SeeAllScreenState.ScreenStatus.FAILED, state.screenStatus)
//            assertEquals(expectedStatus, state.errorStatus)
//
//            viewModel.updateState { SeeAllScreenState() }
//        }
//    }
//
//    @Test
//    fun `handleHomeException should map MovioException correctly`() {
//        val movioException = mockk<MovioException>()
//        every { movioException.code } returns "NETWORK_ERROR"
//
//        val result = viewModel.handleHomeException(movioException)
//
//        assertNotNull(result)
//    }
//
//    @Test
//    fun `handleHomeException should map unknown exception to UNKNOWN_ERROR`() {
//        val result = viewModel.handleHomeException(RuntimeException("unexpected"))
//
//        assertEquals(ErrorStatus.UNKNOWN_ERROR, result)
//    }
//
//    @Test
//    fun `state should handle multiple updates correctly`() = runTest {
//        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.LOADING)
//        viewModel.updateErrorStatus(ErrorStatus.NETWORK_ERROR)
//
//        val state = viewModel.screenState.value
//        assertEquals(SeeAllScreenState.ScreenStatus.LOADING, state.screenStatus)
//        assertEquals(ErrorStatus.NETWORK_ERROR, state.errorStatus)
//
//        viewModel.updateErrorStatus(null)
//        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.SUCCESS)
//
//        val newState = viewModel.screenState.value
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, newState.screenStatus)
//        assertNull(newState.errorStatus)
//    }
//
//    @Test
//    fun `updateState should update screen state correctly`() {
//        val newErrorStatus = ErrorStatus.NETWORK_ERROR
//        viewModel.updateState { it.copy(errorStatus = newErrorStatus) }
//
//        assertEquals(newErrorStatus, viewModel.screenState.value.errorStatus)
//    }
//
//    @Test
//    fun `media content type and media type should be updated correctly`() {
//        viewModel.loadData(MediaContentType.TRENDING, MediaType.SERIES)
//
//        assertEquals(MediaContentType.TRENDING, viewModel.contentType)
//        assertEquals(MediaType.SERIES, viewModel.mediaType)
//    }
//
//    @Test
//    fun `tryToCall should handle onStart callback correctly`() = runTest {
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//
//        assertEquals(SeeAllScreenState.ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
//
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//    }
//
//    @Test
//    fun `onRefresh should call loadData with current contentType and mediaType`() = runTest {
//        viewModel.contentType = MediaContentType.TRENDING
//        viewModel.mediaType = MediaType.SERIES
//
//        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(dummyGenre, dummyGenre2)
//        every { seeAllSeriesPager.getTrendingSeries(any()) } returns flowOf(PagingData.from(listOf(dummySeries)))
//
//        viewModel.onRefresh(1)
//        assertTrue(viewModel.screenState.value.isRefreshing)
//
//        advanceUntilIdle()
//
//        assertFalse(viewModel.screenState.value.isRefreshing)
//        verify { seeAllSeriesPager.getTrendingSeries(dummyGenre.id) }
//    }
//
//    @Test
//    fun `loadDataFlow should handle different mediaType correctly`() = runTest {
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.mediaType = MediaType.MOVIES
//        viewModel.contentType = MediaContentType.TOP_RATING
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        assertNotNull(viewModel.screenState.value.mediaList)
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//
//        every { seeAllSeriesPager.getTopRatingSeries(any()) } returns flowOf(PagingData.from(listOf(dummySeries)))
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.SERIES)
//        advanceUntilIdle()
//
//        assertNotNull(viewModel.screenState.value.mediaList)
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//    }
//
//    @Test
//    fun `concurrent operations should handle state correctly`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadGenres()
//        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.LOADING)
//        viewModel.updateErrorStatus(ErrorStatus.NETWORK_ERROR)
//
//        advanceUntilIdle()
//
//        val finalState = viewModel.screenState.value
//        assertNotNull(finalState.genres)
//        assertEquals(ErrorStatus.NETWORK_ERROR, finalState.errorStatus)
//    }
//
//    @Test
//    fun `refresh with different genre indices should work correctly`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre, dummyGenre2)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        viewModel.onRefresh(0)
//        advanceUntilIdle()
//        assertFalse(viewModel.screenState.value.isRefreshing)
//
//        viewModel.onRefresh(1)
//        advanceUntilIdle()
//        assertFalse(viewModel.screenState.value.isRefreshing)
//        verify { seeAllMoviesPager.getTopRatingMovies(dummyGenre.id) }
//    }
//
//    @Test
//    fun `error handling during refresh should work correctly`() = runTest {
//        val exception = NetworkException()
//        coEvery { manageMoviesUseCase.getMoviesGenres() } throws exception
//
//        viewModel.onRefresh(0)
//
//        assertTrue(viewModel.screenState.value.isRefreshing)
//
//        advanceUntilIdle()
//
//        assertFalse(viewModel.screenState.value.isRefreshing)
//        assertEquals(SeeAllScreenState.ScreenStatus.FAILED, viewModel.screenState.value.screenStatus)
//    }
//
//    @Test
//    fun `loadData with genreId parameter should pass correct genreId to pagers`() = runTest {
//        val testGenreId = 123L
//        every { seeAllMoviesPager.getTopRatingMovies(testGenreId) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES, testGenreId)
//        advanceUntilIdle()
//
//        verify { seeAllMoviesPager.getTopRatingMovies(testGenreId) }
//    }
//
//    @Test
//    fun `multiple genre selections should update state correctly`() = runTest {
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre, dummyGenre2)
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(dummyMovie)))
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        viewModel.onGenreSelected(1)
//        assertEquals(1, viewModel.screenState.value.selectedGenreIndex)
//
//        viewModel.onGenreSelected(0)
//        assertEquals(0, viewModel.screenState.value.selectedGenreIndex)
//
//        viewModel.onGenreSelected(0)
//        assertEquals(0, viewModel.screenState.value.selectedGenreIndex)
//    }
//
//    @Test
//    fun `empty paging data should be handled correctly`() = runTest {
//        every { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.empty())
//        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
//
//        viewModel.loadData(MediaContentType.TOP_RATING, MediaType.MOVIES)
//        advanceUntilIdle()
//
//        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
//        assertNotNull(viewModel.screenState.value.mediaList)
//    }
//}