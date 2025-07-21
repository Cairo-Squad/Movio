package com.cairosquad.viewmodel.home

import com.cairosquad.domain.exception.MovioException
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
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus

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
    private val getRandomSeriesUseCase: GetRandomSeriesUseCase,
    private val getRandomMoviesUseCase: GetRandomMoviesUseCase,



) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        loadRandomSeries()
        loadRandomMovies()
        loadTopRatingMovies()
        loadTrendingMovies()
        loadNowPlayingMovies()
        loadFreeToWatchMovies()
        loadUpcomingMovies()
        loadMoreRecommendedMovies()
        loadTopRatingSeries()
        loadAiringTodaySeries()
        loadOnTvSeries()
        loadMoreRecommendedSeries()
    }

    private fun loadTopRatingMovies() {
        tryToCall(
            block = {
                updateState { it.copy(
                    screenStatus = HomeScreenState.ScreenStatus.LOADING
                ) }
                 getTopRatingMoviesUseCase.getTopRatingMovies(1).map { it.toHomeMovieUiState() }

            },
            onSuccess = { movies ->
                updateState {
                    it.copy(
                        topRatingMovies = movies,
                        screenStatus = HomeScreenState.ScreenStatus.SUCCESS
                    )
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(
                        errorStatus = handleHomeException(throwable),
                        screenStatus = HomeScreenState.ScreenStatus.FAILED
                    )
                }
            },

            )
    }

    private fun loadNowPlayingMovies() {
        tryToCall(
            block = { getNowPlayingMoviesUseCase.getNowPlayingMovies(1).map { it.toHomeMovieUiState() } },
            onSuccess = { movies ->
                updateState {
                    it.copy(nowPlayingMovies = movies)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )


    }

    private fun loadTrendingMovies() {
        tryToCall(
            block = { getTrendingMoviesUseCase .getTrendingMovies(1).map { it.toHomeMovieUiState() }},
            onSuccess = { movies ->
                updateState {
                    it.copy(trendingMovies = movies)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    private fun loadFreeToWatchMovies() {
        tryToCall(
            block = { getFreeToWatchMoviesUseCase.getFreeToWatchMovies(1).map { it.toHomeMovieUiState() }},
            onSuccess = { movies ->
                updateState {
                    it.copy(freeToWatchMovies = movies)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    private fun loadUpcomingMovies() {
        tryToCall(
            block = { getUpcomingMoviesUseCase.getUpcomingMovies(1).map { it.toHomeMovieUiState() }},
            onSuccess = { movies ->
                updateState {
                    it.copy(upcomingMovies = movies)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    private fun loadMoreRecommendedMovies() {
        tryToCall(
            block = { getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(1).map { it.toHomeMovieUiState() } },
            onSuccess = { movies ->
                updateState {
                    it.copy(moreRecommendedMovies = movies)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    private fun loadTopRatingSeries() {
        tryToCall(
            block = { getTopRatingSeriesUseCase.getTopRatingSeries(1)
                .map { it.toHomeSeriesUiState() }
                  },
            onSuccess = { series ->
                updateState {
                    it.copy(topRatingSeries = series)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    private fun loadAiringTodaySeries() {
        tryToCall(
            block = { getAiringTodaySeriesUseCase.getAiringTodaySeries(1).map { it.toHomeSeriesUiState() }},
            onSuccess = { series ->
                updateState {
                    it.copy(airingTodaySeries = series)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },

            )
    }

    private fun loadOnTvSeries() {
        tryToCall(
            block = { getOnTvSeriesUseCase.getOnTvSeries(1).map { it.toHomeSeriesUiState() }},
            onSuccess = { series ->
                updateState {
                    it.copy(onTvSeries = series)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    private fun loadMoreRecommendedSeries() {
        tryToCall(
            block = { getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(1) .map { it.toHomeSeriesUiState() }},
            onSuccess = { series ->
                updateState {
                    it.copy(moreRecommendedSeries = series)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }
    private fun loadRandomMovies() {
        tryToCall(
            block = { getRandomMoviesUseCase.getRandomMovies(1).map { it.toHomeMovieUiState() } },
            onSuccess = { movies ->
                updateState {
                    it.copy(randomMovies = movies)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )


    }

    private fun loadRandomSeries() {
        tryToCall(
            block = {
                getRandomSeriesUseCase.getRandomSeries(1).map { it.toHomeSeriesUiState() }
            },
            onSuccess = { series ->
                updateState {
                    it.copy(randomSeries = series)
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
        )
    }

    override fun onClickProfile() {
        sendEffect(HomeEffect.NavigateToProfile)
    }

    override fun onClickTab(tabType: HomeScreenState.TabType) {
        updateState {
            it.copy(selectedTab = tabType)
        }
    }

    override fun onClickMovie(movieId: Long) {
        sendEffect(HomeEffect.NavigateMovie(movieId))
    }

    override fun onClickSeries(seriesId: Long) {
        sendEffect(HomeEffect.NavigateSeries(seriesId))
    }

    override fun onClickSeeAllTopRated(isMovie: Boolean) {
        sendEffect(HomeEffect.NavigateToSeeAllTopRated(isMovie))
    }

    override fun onClickSeeAllTrending() {
        sendEffect(HomeEffect.NavigateToSeeAllTrending)
    }

    override fun onClickSeeAllFreeToWatch() {
        sendEffect(HomeEffect.NavigateToSeeAllFreeToWatch)
    }

    override fun onClickSeeAllUpcoming() {
        sendEffect(HomeEffect.NavigateToSeeAllUpcoming)
    }

    override fun onClickSeeAllMoreRecommended(isMovie: Boolean) {
        sendEffect(HomeEffect.NavigateToSeeAllMoreRecommended(isMovie))
    }

    override fun onClickSeeAllAiringToday() {
        sendEffect(HomeEffect.NavigateToSeeAllAiringToday)
    }

    override fun onClickSeeAllOnTv() {
        sendEffect(HomeEffect.NavigateToSeeAllOnTv)
    }

    private fun handleHomeException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}