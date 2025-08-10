package com.cairosquad.viewmodel.see_all

import androidx.paging.PagingData
import app.cash.turbine.test
import com.cairosquad.domain.exception.DUnauthorizedException
import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.exception.DomainJsonParsingException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class SeeAllViewModelTest {

    private lateinit var viewModel: SeeAllViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val manageMoviesUseCase = mockk<ManageMoviesUseCase>()
    private val manageSeriesUseCase = mockk<ManageSeriesUseCase>()
    private val seeAllMoviesPager =mockk<SeeAllMoviesPager>()
    private val seeAllSeriesPager =mockk<SeeAllSeriesPager>()
    private val dummyMovie =
        Movie(1, "Title", 8.5f, "path.jpg", listOf(), "overview", 1234567890, 120, "trailer")
    private val dummyGenre = Genre(1, "Action")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = SeeAllViewModel(
            manageMoviesUseCase,
            manageSeriesUseCase,
            seeAllMoviesPager,
            seeAllSeriesPager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getDataFetcher returns correct use cases for TRENDING`() {
        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.TRENDING)
        assertNotNull(movieFetcher)
        assertNotNull(seriesFetcher)
    }

    @Test
    fun `handleError maps exceptions to ErrorStatus correctly`() = runTest {
        val exceptionMapping = mapOf<MovioException, ErrorStatus>(
            NetworkException() to ErrorStatus.NETWORK_ERROR,
            InternetConnectionException() to ErrorStatus.NO_INTERNET,
            UnknownException() to ErrorStatus.UNKNOWN_ERROR,
            DUnauthorizedException() to ErrorStatus.UNAUTHORIZED,
            DomainEmptyResponseException() to ErrorStatus.EMPTY,
            DomainJsonParsingException() to ErrorStatus.PARSING_ERROR
        )

        exceptionMapping.forEach { (exception, expectedStatus) ->
            viewModel::class.java.getDeclaredMethod("handleError", Throwable::class.java).apply {
                isAccessible = true
            }.invoke(viewModel, exception)

            val state = viewModel.screenState.value
            assertEquals(SeeAllScreenState.ScreenStatus.FAILED, state.screenStatus)
            assertEquals(expectedStatus, state.errorStatus)
        }
    }

    @Test
    fun `loadGenres with SERIES updates genre list`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesGenres() } returns listOf(dummyGenre)

        viewModel.mediaType = MediaType.SERIES
        viewModel.loadGenres()

        assertEquals(1, viewModel.screenState.value.genres.size)
    }


    @Test
    fun `initial state is correct`() {
        val initialState = viewModel.screenState.value
        assertEquals(SeeAllScreenState.ScreenStatus.LOADING, initialState.screenStatus)
        assertEquals(1, initialState.genres.size) // Default "All" genre
        assertEquals(0, initialState.selectedGenreIndex)
        // assertTrue(initialState.mediaList.())
        assertNull(initialState.errorStatus)
    }

    @Test
    fun `onGenreSelected updates selectedGenreIndex`() = runTest {
        coEvery { manageMoviesUseCase.getMoviesGenres() } returns listOf(dummyGenre)
        coEvery { manageMoviesUseCase.getTopRatingMovies(any(), any()) } returns listOf(
            dummyMovie
        )

        viewModel.mediaType = MediaType.MOVIES
        viewModel.contentType = MediaContentType.TOP_RATING
        viewModel.loadGenres()

        viewModel.onGenreSelected(1)

        assertEquals(1, viewModel.screenState.value.selectedGenreIndex)
    }

    @Test
    fun `onClickBack sends NavigateBack effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.onClickBack()

            assertEquals(SeeAllEffect.NavigateBack, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `onClickMedia with movie sends correct NavigateMediaDetails effect`() =
        runTest(testDispatcher) {

            viewModel.effect.test {
                viewModel.onClickMedia(123, true)

                val effect = awaitItem()
                assertEquals(SeeAllEffect.NavigateMediaDetails(123, true), effect)
                expectNoEvents()
            }
        }

    @Test
    fun `onClickMedia with series sends correct NavigateMediaDetails effect`() =
        runTest(testDispatcher) {

            viewModel.effect.test {
                viewModel.onClickMedia(456, false)

                val effect = awaitItem()
                assertEquals(SeeAllEffect.NavigateMediaDetails(456, false), effect)
                expectNoEvents()
            }
        }

    @Test
    fun `multiple onClickMedia calls send multiple effects`() = runTest(testDispatcher) {

        viewModel.effect.test {
            viewModel.onClickMedia(1, true)
            viewModel.onClickMedia(2, false)
            viewModel.onClickMedia(3, true)

            assertEquals(SeeAllEffect.NavigateMediaDetails(1, true), awaitItem())
            assertEquals(SeeAllEffect.NavigateMediaDetails(2, false), awaitItem())
            assertEquals(SeeAllEffect.NavigateMediaDetails(3, true), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `getDataFetcher returns correct pairs for all MediaContentTypes`() {
        MediaContentType.entries.forEach { type ->
            val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(type)
            assertNotNull(movieFetcher)
            assertNotNull(seriesFetcher)
        }
    }

    @Test
    fun `handleError maps exceptions to correct ErrorStatus`() = runTest {
        val testCases = mapOf(
            NetworkException() to ErrorStatus.NETWORK_ERROR,
            InternetConnectionException() to ErrorStatus.NO_INTERNET,
            UnknownException() to ErrorStatus.UNKNOWN_ERROR,
            DUnauthorizedException() to ErrorStatus.UNAUTHORIZED,
            DomainEmptyResponseException() to ErrorStatus.EMPTY,
            DomainJsonParsingException() to ErrorStatus.PARSING_ERROR,
            Exception() to ErrorStatus.UNKNOWN_ERROR
        )

        testCases.forEach { (exception, expectedStatus) ->
            viewModel.handleError(exception)

            val state = viewModel.screenState.value
            assertEquals(SeeAllScreenState.ScreenStatus.FAILED, state.screenStatus)
            assertEquals(expectedStatus, state.errorStatus)

            viewModel.updateState { SeeAllScreenState() }
        }
    }

    @Test
    fun `getDataFetcher for TOP_RATING returns correct use cases`() = runTest {
        // Given
        val testPage = 1
        val testGenreId = 123L
        val testMovies = listOf(mockk<Movie>())
        val testSeries = listOf(mockk<Series>())

        coEvery {
            manageMoviesUseCase.getTopRatingMovies(
                testPage, testGenreId
            )
        } returns testMovies
        coEvery {
            manageSeriesUseCase.getTopRatingSeries(
                testPage, testGenreId
            )
        } returns testSeries

        // When
        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.TOP_RATING)

        // Then
        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
    }

    @Test
    fun `getDataFetcher for TRENDING returns correct use cases`() = runTest {

        val testPage = 2
        val testGenreId = 456L
        val testMovies = listOf(mockk<Movie>())
        val testSeries = listOf(mockk<Series>())

        coEvery {
            manageMoviesUseCase.getTrendingMovies(
                testPage, testGenreId
            )
        } returns testMovies
        coEvery {
            manageSeriesUseCase.getTrendingSeries(
                testPage, testGenreId
            )
        } returns testSeries

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.TRENDING)

        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
    }

    @Test
    fun `getDataFetcher for FREE_TO_WATCH returns movies use case and empty series`() = runTest {

        val testPage = 3
        val testGenreId = 789L
        val testMovies = listOf(mockk<Movie>())

        coEvery {
            manageMoviesUseCase.getFreeToWatchMovies(
                testPage, testGenreId
            )
        } returns testMovies

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.FREE_TO_WATCH)

        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
        assertTrue(seriesFetcher(testPage, testGenreId).isEmpty())
    }

    @Test
    fun `getDataFetcher for AIRING_TODAY returns empty movies and series use case`() = runTest {

        val testPage = 4
        val testGenreId = 101L
        val testSeries = listOf(mockk<Series>())

        coEvery {
            manageSeriesUseCase.getAiringTodaySeries(
                testPage, testGenreId
            )
        } returns testSeries

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.AIRING_TODAY)

        assertTrue(movieFetcher(testPage, testGenreId).isEmpty())
        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
    }

    @Test
    fun `getDataFetcher for all MediaContentTypes returns non-null functions`() {
        MediaContentType.entries.forEach { type ->
            val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(type)
            assertNotNull(movieFetcher)
            assertNotNull(seriesFetcher)
        }
    }

    @Test
    fun `getDataFetcher handles null genreId correctly`() = runTest {
        val testPage = 1
        val testMovies = listOf(mockk<Movie>())

        coEvery { manageMoviesUseCase.getTopRatingMovies(testPage, null) } returns testMovies

        val (movieFetcher, _) = viewModel.getDataFetcher(MediaContentType.TOP_RATING)

        assertEquals(testMovies, movieFetcher(testPage, null))
    }

    @Test
    fun `getDataFetcher for UPCOMING returns movies use case and empty series`() = runTest {

        val testPage = 1
        val testGenreId = 123L
        val testMovies = listOf(mockk<Movie>())

        coEvery {
            manageMoviesUseCase.getUpcomingMovies(
                testPage, testGenreId
            )
        } returns testMovies

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.UPCOMING)

        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
        assertTrue(seriesFetcher(testPage, testGenreId).isEmpty())
    }

    @Test
    fun `getDataFetcher for NOW_PLAYING returns movies use case and empty series`() = runTest {

        val testPage = 2
        val testGenreId = 456L
        val testMovies = listOf(mockk<Movie>())

        coEvery {
            manageMoviesUseCase.getNowPlayingMovies(
                testPage, testGenreId
            )
        } returns testMovies

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.NOW_PLAYING)

        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
        assertTrue(seriesFetcher(testPage, testGenreId).isEmpty())
    }

    @Test
    fun `getDataFetcher for MORE_RECOMMENDED returns both movies and series use cases`() = runTest {
        val testPage = 3
        val testGenreId = 789L
        val testMovies = listOf(mockk<Movie>())
        val testSeries = listOf(mockk<Series>())

        coEvery {
            manageMoviesUseCase.getMoreRecommendedMovies(
                testPage, testGenreId
            )
        } returns testMovies
        coEvery {
            manageSeriesUseCase.getMoreRecommendedSeries(
                testPage, testGenreId
            )
        } returns testSeries

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.MORE_RECOMMENDED)

        assertEquals(testMovies, movieFetcher(testPage, testGenreId))
        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
    }

    @Test
    fun `getDataFetcher for ON_TV returns empty movies and series use case`() = runTest {
        val testPage = 5
        val testGenreId = 112L
        val testSeries = listOf(mockk<Series>())

        coEvery {
            manageSeriesUseCase.getOnTvSeries(
                testPage, testGenreId
            )
        } returns testSeries

        val (movieFetcher, seriesFetcher) = viewModel.getDataFetcher(MediaContentType.ON_TV)

        assertTrue(movieFetcher(testPage, testGenreId).isEmpty())
        assertEquals(testSeries, seriesFetcher(testPage, testGenreId))
    }

    @Test
    fun `getDataFetcher handles null genreId correctly for all types`() = runTest {
        listOf(
            MediaContentType.UPCOMING,
            MediaContentType.NOW_PLAYING,
            MediaContentType.MORE_RECOMMENDED
        ).forEach { type ->
            val testPage = 1
            val testMovies = listOf(mockk<Movie>())

            when (type) {
                MediaContentType.UPCOMING -> coEvery {
                    manageMoviesUseCase.getUpcomingMovies(
                        testPage, null
                    )
                } returns testMovies

                MediaContentType.NOW_PLAYING -> coEvery {
                    manageMoviesUseCase.getNowPlayingMovies(
                        testPage, null
                    )
                } returns testMovies

                MediaContentType.MORE_RECOMMENDED -> coEvery {
                    manageMoviesUseCase.getMoreRecommendedMovies(
                        testPage, null
                    )
                } returns testMovies

                else -> {}
            }

            val (movieFetcher, _) = viewModel.getDataFetcher(type)
            assertEquals(testMovies, movieFetcher(testPage, null))
        }
        listOf(
            MediaContentType.MORE_RECOMMENDED, MediaContentType.AIRING_TODAY, MediaContentType.ON_TV
        ).forEach { type ->
            val testPage = 1
            val testSeries = listOf(mockk<Series>())

            when (type) {
                MediaContentType.MORE_RECOMMENDED -> coEvery {
                    manageSeriesUseCase.getMoreRecommendedSeries(
                        testPage, null
                    )
                } returns testSeries

                MediaContentType.AIRING_TODAY -> coEvery {
                    manageSeriesUseCase.getAiringTodaySeries(
                        testPage, null
                    )
                } returns testSeries

                MediaContentType.ON_TV -> coEvery {
                    manageSeriesUseCase.getOnTvSeries(
                        testPage, null
                    )
                } returns testSeries

                else -> {}
            }

            val (_, seriesFetcher) = viewModel.getDataFetcher(type)
            assertEquals(testSeries, seriesFetcher(testPage, null))
        }
    }
    @Test
    fun `onRefresh updates isRefreshing state`() = runTest {
        val spyViewModel = spyk(viewModel)
        coEvery { spyViewModel.loadData(any(), any(), any()) } just Runs
        val dummyGenreUiState = SeeAllScreenState.GenreUiState(id = 1, name = "Action")
        viewModel.updateState { it.copy(genres = listOf(dummyGenreUiState)) }

        spyViewModel.onRefresh(0)

        advanceTimeBy(100)
        assertTrue(spyViewModel.screenState.value.isRefreshing)

        advanceTimeBy(500)
        assertEquals(spyViewModel.screenState.value.isRefreshing,false)
    }
    @Test
    fun `updateScreenStatus updates the screen status`() {
        viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.SUCCESS)
        assertEquals(SeeAllScreenState.ScreenStatus.SUCCESS, viewModel.screenState.value.screenStatus)
    }

    @Test
    fun `updateErrorStatus updates the error status`() {
        viewModel.updateErrorStatus(ErrorStatus.NO_INTERNET)
        assertEquals(ErrorStatus.NO_INTERNET, viewModel.screenState.value.errorStatus)
    }

    @Test
    fun `getDataPagerFetcher2 for TOP_RATING returns correct flows`() = runTest {
        val movie = mockk<Movie>()
        val series = mockk<Series>()
        coEvery { seeAllMoviesPager.getTopRatingMovies(any()) } returns flowOf(PagingData.from(listOf(movie)))
        coEvery { seeAllSeriesPager.getTopRatingSeries(any()) } returns flowOf(PagingData.from(listOf(series)))

        val (movieFlow, seriesFlow) = viewModel.getDataPagerFetcher2(MediaContentType.TOP_RATING, 1L)
        val movieResult = movieFlow().first()
        val seriesResult = seriesFlow().first()

        assertNotNull(movieResult)
        assertNotNull(seriesResult)
    }


}