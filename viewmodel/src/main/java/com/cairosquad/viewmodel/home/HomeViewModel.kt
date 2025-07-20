package com.cairosquad.viewmodel.home

import com.cairosquad.domain.exception.MovioException
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
    private val getTopRatingSeriesUseCase: GetTopRatingSeriesUseCase


)  : BaseViewModel<HomeScreenState,HomeEffect>(HomeScreenState()),
    HomeInteractionsListener{
    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        loadTopRatingMovies()
        loadTrendingMovies()
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
            onStart = {
                updateState { it.copy(screenStatus = HomeScreenState.ScreenStatus.LOADING) }
            },
            block = { getTopRatingMoviesUseCase() },
            onSuccess = { movies ->
                updateState {
                    it.copy(
                        topRatingMovies = movies.map { it.toHomeMovieUiState() },
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
    private fun loadNowPlayingMovies()
    {
        tryToCall(
            block = {getNowPlayingMoviesUseCase()},
            onSuccess = { movies ->
                updateState {
                    it.copy(nowPlayingMovies=movies.map{it.toHomeMovieUiState()})
                }
            },
            onError =  { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },


    }
    private fun loadTrendingMovies() {
        tryToCall(
            block = { getTrendingMoviesUseCase() },
            onSuccess = { movies ->
                updateState {
                    it.copy(trendingMovies = movies.map { it.toHomeMovieUiState() })
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
            block = { getFreeToWatchMoviesUseCase() },
            onSuccess = { movies ->
                updateState {
                    it.copy(freeToWatchMovies = movies.map { it.toHomeMovieUiState() })
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
            block = { getUpcomingMoviesUseCase() },
            onSuccess = { movies ->
                updateState {
                    it.copy(upcomingMovies = movies.map { it.toHomeMovieUiState() })
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
            block = { getMoreRecommendedMoviesUseCase() },
            onSuccess = { movies ->
                updateState {
                    it.copy(moreRecommendedMovies = movies.map { it.toHomeMovieUiState() })
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun loadTopRatingSeries() {
        tryToCall(
            block = { getTopRatingSeriesUseCase() },
            onSuccess = { series ->
                updateState {
                    it.copy(topRatingSeries = series.map { it.toHomeSeriesUiState() })
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
            block = { getAiringTodaySeriesUseCase() },
            onSuccess = { series ->
                updateState {
                    it.copy(airingTodaySeries = series.map { it.toHomeSeriesUiState() })
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
            block = { getOnTvSeriesUseCase() },
            onSuccess = { series ->
                updateState {
                    it.copy(onTvSeries = series.map { it.toHomeSeriesUiState() })
                }
            },
            onError = { throwable ->
                updateState {
                    it.copy(errorStatus = handleHomeException(throwable))
                }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun loadMoreRecommendedSeries() {
        tryToCall(
            block = { getMoreRecommendedSeriesUseCase() },
            onSuccess = { series ->
                updateState {
                    it.copy(moreRecommendedSeries = series.map { it.toHomeSeriesUiState() })
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

    override fun onClickCategory(category: String) {
        sendEffect(HomeEffect.NavigateToCategory(category))
    }

    override fun onClickMovie(movieId: Long) {
        sendEffect(HomeEffect.NavigateMovie(movieId))
    }

    override fun onClickSeries(seriesId: Long) {
        sendEffect(HomeEffect.NavigateSeries(seriesId))
    }

    override fun onClickSeeAllTopRated(isMovie :Boolean ) {
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

    override fun onClickSeeAllMoreRecommended(isMovie:Boolean) {
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