package com.cairosquad.viewmodel.home

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.jupiter.api.Test
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetRandomMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
import com.cairosquad.domain.usecase.series.GetRandomSeriesUseCase
import com.cairosquad.domain.usecase.series.GetTopRatingSeriesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import org.junit.Before


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var getFreeToWatchMovies: GetFreeToWatchMoviesUseCase
    private lateinit var getMoreRecommendedMovies: GetMoreRecommendedMoviesUseCase
    private lateinit var getTopRatingMovies: GetTopRatingMoviesUseCase
    private lateinit var getTrendingMovies: GetTrendingMoviesUseCase
    private lateinit var getUpcomingMovies: GetUpcomingMoviesUseCase
    private lateinit var getNowPlayingMovies: GetNowPlayingMoviesUseCase
    private lateinit var getAiringTodaySeries: GetAiringTodaySeriesUseCase
    private lateinit var getMoreRecommendedSeries: GetMoreRecommendedSeriesUseCase
    private lateinit var getOnTvSeries: GetOnTvSeriesUseCase
    private lateinit var getTopRatingSeries: GetTopRatingSeriesUseCase
    private lateinit var getRandomSeries: GetRandomSeriesUseCase
    private lateinit var getRandomMovies: GetRandomMoviesUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)



    }


    @Test
    fun shouldReturnTopRatingMoviesOnInit() = runTest {
        // Arrange
        getTopRatingMovies = mockk()
        coEvery { getTopRatingMovies.getTopRatingMovies(1) } returns listOf(dummyMovie)

        // باقي اليوز كيسز هنسيبهم mocked فاضيين (ممكن تعملهم mockk(relaxed = true) أو بس dummy mocks)
        getFreeToWatchMovies = mockk(relaxed = true)
        getMoreRecommendedMovies = mockk(relaxed = true)
        getTrendingMovies = mockk(relaxed = true)
        getUpcomingMovies = mockk(relaxed = true)
        getNowPlayingMovies = mockk(relaxed = true)
        getAiringTodaySeries = mockk(relaxed = true)
        getMoreRecommendedSeries = mockk(relaxed = true)
        getOnTvSeries = mockk(relaxed = true)
        getTopRatingSeries = mockk(relaxed = true)
        getRandomSeries = mockk(relaxed = true)
        getRandomMovies = mockk(relaxed = true)

        viewModel = HomeViewModel(
            getFreeToWatchMovies,
            getMoreRecommendedMovies,
            getTopRatingMovies,
            getTrendingMovies,
            getUpcomingMovies,
            getNowPlayingMovies,
            getAiringTodaySeries,
            getMoreRecommendedSeries,
            getOnTvSeries,
            getTopRatingSeries,
            getRandomSeries,
            getRandomMovies,
        )

        // Assert
        val state = viewModel.screenState.value
        assertThat(state.topRatingMovies).isEqualTo(listOf(dummyMovie))
    }


    private companion object {
        val dummyMovie = com.cairosquad.entity.Movie(
            id = 1,
            title = "Test Movie",
            rating = 5f,
            posterPath = "/img.jpg",
            genres = emptyList(),
            overview = "Overview",
            releaseDate = 0L,
            runtimeMinutes = 120,
            trailerPath = ""
        )

        val dummySeries = com.cairosquad.entity.Series(
            id = 1,
            title = "Test Series",
            rating = 4.5f,
            posterPath = "/img.jpg",
            trailerPath = "",
            genres = emptyList(),
            overview = "Overview",
            releaseDate = 0L,
            seasonsCount = 3
        )
    }
}