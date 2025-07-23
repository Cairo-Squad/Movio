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
import com.cairosquad.viewmodel.home.effect.DiscoverEffect
import com.cairosquad.viewmodel.home.listner.DiscoverInteractionsListener
import com.cairosquad.viewmodel.home.model.DiscoverType
import com.cairosquad.viewmodel.home.model.MediaType
import com.cairosquad.viewmodel.home.state.DiscoverScreenState
import com.cairosquad.viewmodel.home.state.toDiscoverGenreUiState
import com.cairosquad.viewmodel.home.state.toDiscoverMovieUiState
import com.cairosquad.viewmodel.home.state.toDiscoverSeriesUiState

class DiscoverViewModel( private val getFreeToWatchMoviesUseCase: GetFreeToWatchMoviesUseCase,
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
) : BaseViewModel<DiscoverScreenState, DiscoverEffect>(initialState = DiscoverScreenState()),
    DiscoverInteractionsListener {

    init {
        loadGenres()
    }

    override fun onGenreSelected(genreIndex: Int) {
        updateState {
            it.copy(selectedGenreIndex = genreIndex)
        }
    }

    override fun onFilterSelected(filter: DiscoverScreenState.FilterType) {
        updateState {
            it.copy(selectedFilter = filter)
        }
    }

    override fun onClickMovie(movieId: Long) {
        sendEffect(DiscoverEffect.NavigateMovie(movieId))
    }

    override fun onClickSeries(seriesId: Long) {
        sendEffect(DiscoverEffect.NavigateSeries(seriesId))
    }

    fun loadDiscoverContent(type: DiscoverType, mediaType: MediaType) {
        when (type) {
            DiscoverType.TRENDING -> {
                if (mediaType != MediaType.Series) loadTrendingMovies()
                if (mediaType != MediaType.Movies) {}
            }
            DiscoverType.TOP_RATING -> {
                if (mediaType != MediaType.Series) loadTopRatingMovies()
                if (mediaType != MediaType.Movies) loadTopRatingSeries()
            }
            DiscoverType.MORE_RECOMMENDED -> {
                if (mediaType != MediaType.Series) loadMoreRecommendedMovies()
                if (mediaType != MediaType.Movies) loadMoreRecommendedSeries()
            }
            DiscoverType.FREE_TO_WATCH -> {
                if (mediaType != MediaType.Series) loadFreeToWatchMovies()
                if (mediaType != MediaType.Movies) {}
            }
            DiscoverType.UPCOMING -> {
                if (mediaType != MediaType.Series) loadUpcomingMovies()
                if (mediaType != MediaType.Movies) {}
            }
        }
    }
    private fun loadTopRatingMovies() {
        updateState { it.copy(screenStatus =  DiscoverScreenState.ScreenStatus.LOADING) }
        fetchData(
            block = { getTopRatingMoviesUseCase.getTopRatingMovies(1) },
            mapper = { it.toDiscoverMovieUiState() },
            update = { state, result ->
                state.copy(
                    topRatedMovies = result,
                    screenStatus = DiscoverScreenState.ScreenStatus.SUCCESS
                )
            }
        )
    }




    private fun loadTrendingMovies() = fetchData(
        block = { getTrendingMoviesUseCase.getTrendingMovies(1) },
        mapper = { it.toDiscoverMovieUiState() },
        update = { state, result -> state.copy(trendingMovies = result) }
    )

    private fun loadFreeToWatchMovies() = fetchData(
        block = { getFreeToWatchMoviesUseCase.getFreeToWatchMovies(1) },
        mapper = { it.toDiscoverMovieUiState() },
        update = { state, result -> state.copy(freeToWatchMovies = result) }
    )

    private fun loadUpcomingMovies() = fetchData(
        block = { getUpcomingMoviesUseCase.getUpcomingMovies(1) },
        mapper = { it.toDiscoverMovieUiState() },
        update = { state, result -> state.copy(upcomingMovies = result) }
    )

    private fun loadMoreRecommendedMovies() = fetchData(
        block = { getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(1) },
        mapper = { it.toDiscoverMovieUiState() },
        update = { state, result -> state.copy(moreRecommendedMovies = result) }
    )

    private fun loadTopRatingSeries() = fetchData(
        block = { getTopRatingSeriesUseCase.getTopRatingSeries(1) },
        mapper = { it.toDiscoverSeriesUiState() },
        update = { state, result -> state.copy(topRatedSeries = result) }
    )

//    private fun loadAiringTodaySeries() = fetchData(
//        block = { getAiringTodaySeriesUseCase.getAiringTodaySeries(1) },
//        mapper = { it.toDiscoverSeriesUiState() },
//        update = { state, result -> state.copy(a = result) }
//    )

//    private fun loadOnTvSeries() = fetchData(
//        block = { getOnTvSeriesUseCase.getOnTvSeries(1) },
//        mapper = { it.toHomeSeriesUiState() },
//        update = { state, result -> state.copy(onTvSeries = result) }
//    )

    private fun loadMoreRecommendedSeries() = fetchData(
        block = { getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(1) },
        mapper = { it.toDiscoverSeriesUiState() },
        update = { state, result -> state.copy(moreRecommendedSeries = result) }
    )

//    private fun loadPopularMovies() = fetchData(
//        block = { getPopularMoviesUseCase.getPopularMovies(page = 1) },
//        mapper = { it.toDiscoverMovieUiState() },
//        update = { state, result -> state.copy(po = result) }
//    )

//    private fun loadPopularSeries() = fetchData(
//        block = { getPopularSeriesUseCase.getPopularSeries(1) },
//        mapper = { it.toDiscoverSeriesUiState() },
//        update = { state, result -> state.copy(popularSeries = result) }
//    )

    private fun loadGenres() {
        tryToCall(
            block = {
                val movieGenres = getMoviesGenresUseCase.getMoviesGenres()
                val seriesGenres = getSeriesGenresUseCase.getSeriesGenres()

                val combined = buildSet {
                    add(DiscoverScreenState.GenreUiState.defaultGenre)
                    movieGenres.mapTo(this) { it.toDiscoverGenreUiState() }
                    seriesGenres.mapTo(this) { it.toDiscoverGenreUiState() }
                }

                combined
            },
            onSuccess = { genres ->
                updateState { it.copy(genres = genres.toList()) }
            },
            onError = ::handleError
        )
    }
    private fun <T, R> fetchData(
        block: suspend () -> List<T>,
        mapper: (T) -> R,
        update: (DiscoverScreenState, List<R>) -> DiscoverScreenState
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
        update: (DiscoverScreenState, List<R>) -> DiscoverScreenState
    ) {
        updateState { currentState -> update(currentState, result) }
    }

    private fun handleError(throwable: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(throwable),
                screenStatus = DiscoverScreenState.ScreenStatus.FAILED
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
