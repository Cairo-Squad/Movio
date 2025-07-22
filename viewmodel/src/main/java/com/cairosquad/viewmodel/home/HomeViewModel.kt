package com.cairosquad.viewmodel.home

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
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
) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
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

    override fun onClickSeeAllTopRated(isMovie: Boolean) {
        updateState { it.copy(seeAllType = "Top Rated") }
        sendEffect(HomeEffect.NavigateToSeeAllTopRated(isMovie))
    }

    override fun onClickSeeAllTrending() {
        updateState { it.copy(seeAllType = "Trending") }
        sendEffect(HomeEffect.NavigateToSeeAllTrending)
    }

    override fun onClickSeeAllFreeToWatch() {
        updateState { it.copy(seeAllType = "Free To Watch") }
        sendEffect(HomeEffect.NavigateToSeeAllFreeToWatch)
    }

    override fun onClickSeeAllUpcoming() {
        updateState { it.copy(seeAllType = "Upcoming") }
        sendEffect(HomeEffect.NavigateToSeeAllUpcoming)
    }

    override fun onClickSeeAllMoreRecommended(isMovie: Boolean) {
        updateState { it.copy(seeAllType = "More Recommended") }
        sendEffect(HomeEffect.NavigateToSeeAllMoreRecommended(isMovie))
    }

    override fun onClickSeeAllAiringToday() {
        updateState { it.copy(seeAllType = "Airing Today") }
        sendEffect(HomeEffect.NavigateToSeeAllAiringToday)
    }

    override fun onClickSeeAllOnTv() {
        updateState { it.copy(seeAllType = "On Tv") }
        sendEffect(HomeEffect.NavigateToSeeAllOnTv)
    }

    override fun onClickBackInSeeAllScreen(){
        updateState { it.copy(seeAllType = null) }
    }

    override fun onClickCategoryChip(categoryChipIndex: Int) {
       updateState { it.copy(selectedCategoriesChip =categoryChipIndex ) }
    }

    override fun onClickSortChip(sortChipIndex: Int) {
        updateState { it.copy(selectedSortChip =sortChipIndex ) }
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