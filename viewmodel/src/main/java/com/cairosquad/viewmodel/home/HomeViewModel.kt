package com.cairosquad.viewmodel.home

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoviesGenresUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetPopularMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
import com.cairosquad.domain.usecase.series.GetPopularSeriesUseCase
import com.cairosquad.domain.usecase.series.GetSeriesGenresUseCase
import com.cairosquad.domain.usecase.series.GetTopRatingSeriesUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.home.effect.HomeEffect
import com.cairosquad.viewmodel.home.listner.HomeInteractionsListener
import com.cairosquad.viewmodel.home.state.HomeScreenState

class HomeViewModel(
    private val getFreeToWatchMoviesUseCase: GetFreeToWatchMoviesUseCase,
    private val getMoreRecommendedMoviesUseCase: GetMoreRecommendedMoviesUseCase,
    private val getTopRatingMoviesUseCase: GetTopRatingMoviesUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getAiringTodaySeriesUseCase: GetAiringTodaySeriesUseCase,
    private val getMoreRecommendedSeriesUseCase: GetMoreRecommendedSeriesUseCase,
    private val getOnTvSeriesUseCase: GetOnTvSeriesUseCase,
    private val getTopRatingSeriesUseCase: GetTopRatingSeriesUseCase,
    private val getPopularSeriesUseCase: GetPopularSeriesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getMoviesGenresUseCase: GetMoviesGenresUseCase,
    private val getSeriesGenresUseCase: GetSeriesGenresUseCase,
) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadPopularSeries()
        loadPopularMovies()

        loadTopRatingMovies()
        loadTrendingMovies()

//        loadNowPlayingMovies()
//        loadFreeToWatchMovies()
//        loadUpcomingMovies()
//        loadMoreRecommendedMovies()
//        loadTopRatingSeries()
//        loadAiringTodaySeries()
//        loadOnTvSeries()
//        loadMoreRecommendedSeries()
       loadGenres()
    }


    private fun loadTopRatingMovies() {
        updateState { it.copy(screenStatus = HomeScreenState.ScreenStatus.LOADING) }
        fetchData(
            block = { getTopRatingMoviesUseCase.getTopRatingMovies(1) },
            mapper = { it.toHomeMovieUiState() },
            update = { state, result ->
                state.copy(
                    topRatingMovies = result,
                    screenStatus = HomeScreenState.ScreenStatus.SUCCESS
                )
            }
        )
    }


    private fun loadNowPlayingMovies() = fetchData(
        block = { getNowPlayingMoviesUseCase.getNowPlayingMovies(1) },
        mapper = { it.toHomeMovieUiState() },
        update = { state, result -> state.copy(nowPlayingMovies = result) }
    )

    private fun loadTrendingMovies() = fetchData(
        block = { getTrendingMoviesUseCase.getTrendingMovies(1) },
        mapper = { it.toHomeMovieUiState() },
        update = { state, result -> state.copy(trendingMovies = result) }
    )

    private fun loadFreeToWatchMovies() = fetchData(
        block = { getFreeToWatchMoviesUseCase.getFreeToWatchMovies(1) },
        mapper = { it.toHomeMovieUiState() },
        update = { state, result -> state.copy(freeToWatchMovies = result) }
    )

    private fun loadUpcomingMovies() = fetchData(
        block = { getUpcomingMoviesUseCase.getUpcomingMovies(1) },
        mapper = { it.toHomeMovieUiState() },
        update = { state, result -> state.copy(upcomingMovies = result) }
    )

    private fun loadMoreRecommendedMovies() = fetchData(
        block = { getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(1) },
        mapper = { it.toHomeMovieUiState() },
        update = { state, result -> state.copy(moreRecommendedMovies = result) }
    )

    private fun loadTopRatingSeries() = fetchData(
        block = { getTopRatingSeriesUseCase.getTopRatingSeries(1) },
        mapper = { it.toHomeSeriesUiState() },
        update = { state, result -> state.copy(topRatingSeries = result) }
    )

    private fun loadAiringTodaySeries() = fetchData(
        block = { getAiringTodaySeriesUseCase.getAiringTodaySeries(1) },
        mapper = { it.toHomeSeriesUiState() },
        update = { state, result -> state.copy(airingTodaySeries = result) }
    )

    private fun loadOnTvSeries() = fetchData(
        block = { getOnTvSeriesUseCase.getOnTvSeries(1) },
        mapper = { it.toHomeSeriesUiState() },
        update = { state, result -> state.copy(onTvSeries = result) }
    )

    private fun loadMoreRecommendedSeries() = fetchData(
        block = { getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(1) },
        mapper = { it.toHomeSeriesUiState() },
        update = { state, result -> state.copy(moreRecommendedSeries = result) }
    )

    private fun loadPopularMovies() = fetchData(
        block = { getPopularMoviesUseCase.getPopularMovies(page = 1) },
        mapper = { it.toHomeMovieUiState() },
        update = { state, result -> state.copy(popularMovies = result) }
    )

    private fun loadPopularSeries() = fetchData(
        block = { getPopularSeriesUseCase.getPopularSeries(1) },
        mapper = { it.toHomeSeriesUiState() },
        update = { state, result -> state.copy(popularSeries = result) }
    )

    private fun loadGenres() {
        tryToCall(
            block = {
                val movieGenres = getMoviesGenresUseCase.getMoviesGenres()
                val seriesGenres = getSeriesGenresUseCase.getSeriesGenres()

                val combined = buildSet {
                    add(HomeScreenState.GenreUiState.defaultGenre)
                    movieGenres.mapTo(this) { it.toHomeGenreUiState() }
                    seriesGenres.mapTo(this) { it.toHomeGenreUiState() }
                }

                combined
            },
            onSuccess = { genres ->
                updateState { it.copy(genres = genres.toList()) }
            },
            onError = ::handleError
        )
    }

    override fun onClickProfile() {
        sendEffect(HomeEffect.NavigateToProfile)
    }

    override fun onClickTab(tabIndex: Int) {
        updateState {
            it.copy(selectedTab = HomeScreenState.TabType.entries[tabIndex])
        }
    }

    override fun onClickMovie(movieId: Long) {
        sendEffect(HomeEffect.NavigateMovie(movieId))
    }

    override fun onClickSeries(seriesId: Long) {
        sendEffect(HomeEffect.NavigateSeries(seriesId))
    }

    override fun onClickSeeAllAiringToday() {
        sendEffect(HomeEffect.NavigateToSeeAllAiringToday)
    }

    override fun onClickSeeAllOnTv() {
        sendEffect(HomeEffect.NavigateToSeeAllOnTv)
    }

    override fun onGenreSelected(genreIndex: Int) {
        updateState {
            it.copy(selectedGenreIndex = genreIndex)
        }
    }

    override fun onFilterSelected(filter: HomeScreenState.FilterType) {
        updateState {
            it.copy(selectedFilter = filter)
        }

    }


    private fun <T, R> fetchData(
        block: suspend () -> List<T>,
        mapper: (T) -> R,
        update: (HomeScreenState, List<R>) -> HomeScreenState
    ) {
        tryToCall(
            block = { mapResult(block, mapper) },
            onSuccess = { result -> handleSuccess(result, update) },
            onError = ::handleError
        )
    }

    private suspend fun <T, R> mapResult(
        block: suspend () -> List<T>,
        mapper: (T) -> R
    ): List<R> = block().map(mapper)

    private fun <R> handleSuccess(
        result: List<R>,
        update: (HomeScreenState, List<R>) -> HomeScreenState
    ) {
        updateState { currentState -> update(currentState, result) }
    }

    private fun handleError(throwable: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(throwable),
                screenStatus = HomeScreenState.ScreenStatus.FAILED
            )
        }
    }

    private fun handleHomeException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> exceptionToErrorStatus(e)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }

}