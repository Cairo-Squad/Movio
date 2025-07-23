package com.cairosquad.viewmodel.see_all

import app.cash.turbine.test
import com.cairosquad.domain.exception.DUnauthorizedException
import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.exception.DomainJsonParsingException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoviesGenresUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
import com.cairosquad.domain.usecase.series.GetSeriesGenresUseCase
import com.cairosquad.domain.usecase.series.GetTopRatingSeriesUseCase
import com.cairosquad.domain.usecase.series.GetTrendingSeriesUseCase
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class SeeAllViewModelTest {

    private lateinit var viewModel: SeeAllViewModel

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()
    private val getFreeToWatchMoviesUseCase = mockk<GetFreeToWatchMoviesUseCase>()
    private val getMoreRecommendedMoviesUseCase = mockk<GetMoreRecommendedMoviesUseCase>()
    private val getTopRatingMoviesUseCase = mockk<GetTopRatingMoviesUseCase>()
    private val getTrendingMoviesUseCase = mockk<GetTrendingMoviesUseCase>()
    private val getUpcomingMoviesUseCase = mockk<GetUpcomingMoviesUseCase>()
    private val getNowPlayingMoviesUseCase = mockk<GetNowPlayingMoviesUseCase>()
    private val getAiringTodaySeriesUseCase = mockk<GetAiringTodaySeriesUseCase>()
    private val getMoreRecommendedSeriesUseCase = mockk<GetMoreRecommendedSeriesUseCase>()
    private val getOnTvSeriesUseCase = mockk<GetOnTvSeriesUseCase>()
    private val getTopRatingSeriesUseCase = mockk<GetTopRatingSeriesUseCase>()
    private val getMoviesGenresUseCase = mockk<GetMoviesGenresUseCase>()
    private val getSeriesGenresUseCase = mockk<GetSeriesGenresUseCase>()
    private val getTrendingSeriesUseCase = mockk<GetTrendingSeriesUseCase>()

    private val dummyMovie =
        Movie(1, "Title", 8.5f, "path.jpg", listOf(), "overview", 1234567890, 120, "trailer")
    private val dummyGenre = Genre(1, "Action")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = SeeAllViewModel(
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
            getMoviesGenresUseCase,
            getSeriesGenresUseCase,
            getTrendingSeriesUseCase
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
    fun `combineTwoList merges lists alternately`() {
        val movies = listOf("A", "B")
        val series = listOf("1", "2", "3")
        val result = viewModel.run {
            val m = this::class.java.getDeclaredMethod(
                "combineTwoList", List::class.java, List::class.java
            )
            m.isAccessible = true
            m.invoke(this, movies, series) as List<*>
        }
        assertEquals(listOf("A", "1", "B", "2", "3"), result)
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
        coEvery { getSeriesGenresUseCase.getSeriesGenres() } returns listOf(dummyGenre)

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
        assertTrue(initialState.mediaList.isEmpty())
        assertNull(initialState.errorStatus)
    }

    @Test
    fun `onGenreSelected updates selectedGenreIndex`() = runTest {
        coEvery { getMoviesGenresUseCase.getMoviesGenres() } returns listOf(dummyGenre)
        coEvery { getTopRatingMoviesUseCase.getTopRatingMovies(any(), any()) } returns listOf(
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
            getTopRatingMoviesUseCase.getTopRatingMovies(
                testPage, testGenreId.toString()
            )
        } returns testMovies
        coEvery {
            getTopRatingSeriesUseCase.getTopRatingSeries(
                testPage, testGenreId.toString()
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
            getTrendingMoviesUseCase.getTrendingMovies(
                testPage, testGenreId.toString()
            )
        } returns testMovies
        coEvery {
            getTrendingSeriesUseCase.getTrendingSeries(
                testPage, testGenreId.toString()
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
            getFreeToWatchMoviesUseCase.getFreeToWatchMovies(
                testPage, testGenreId.toString()
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
            getAiringTodaySeriesUseCase.getAiringTodaySeries(
                testPage, testGenreId.toString()
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

        coEvery { getTopRatingMoviesUseCase.getTopRatingMovies(testPage, null) } returns testMovies

        val (movieFetcher, _) = viewModel.getDataFetcher(MediaContentType.TOP_RATING)

        assertEquals(testMovies, movieFetcher(testPage, null))
    }

    @Test
    fun `getDataFetcher for UPCOMING returns movies use case and empty series`() = runTest {

        val testPage = 1
        val testGenreId = 123L
        val testMovies = listOf(mockk<Movie>())

        coEvery {
            getUpcomingMoviesUseCase.getUpcomingMovies(
                testPage, testGenreId.toString()
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
            getNowPlayingMoviesUseCase.getNowPlayingMovies(
                testPage, testGenreId.toString()
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
            getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(
                testPage, testGenreId.toString()
            )
        } returns testMovies
        coEvery {
            getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(
                testPage, testGenreId.toString()
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
            getOnTvSeriesUseCase.getOnTvSeries(
                testPage, testGenreId.toString()
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
                    getUpcomingMoviesUseCase.getUpcomingMovies(
                        testPage, null
                    )
                } returns testMovies

                MediaContentType.NOW_PLAYING -> coEvery {
                    getNowPlayingMoviesUseCase.getNowPlayingMovies(
                        testPage, null
                    )
                } returns testMovies

                MediaContentType.MORE_RECOMMENDED -> coEvery {
                    getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(
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
                    getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(
                        testPage, null
                    )
                } returns testSeries

                MediaContentType.AIRING_TODAY -> coEvery {
                    getAiringTodaySeriesUseCase.getAiringTodaySeries(
                        testPage, null
                    )
                } returns testSeries

                MediaContentType.ON_TV -> coEvery {
                    getOnTvSeriesUseCase.getOnTvSeries(
                        testPage, null
                    )
                } returns testSeries

                else -> {}
            }

            val (_, seriesFetcher) = viewModel.getDataFetcher(type)
            assertEquals(testSeries, seriesFetcher(testPage, null))
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}