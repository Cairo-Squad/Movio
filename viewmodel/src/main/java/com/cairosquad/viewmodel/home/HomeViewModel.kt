package com.cairosquad.viewmodel.home

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.movies.GetAllMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoviesGenresUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetPopularMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetAllSeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
import com.cairosquad.domain.usecase.series.GetPopularSeriesUseCase
import com.cairosquad.domain.usecase.series.GetSeriesGenresUseCase
import com.cairosquad.domain.usecase.series.GetTopRatingSeriesUseCase
import com.cairosquad.domain.usecase.series.GetTrendingSeriesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

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
    private val getTrendingSeriesUseCase: GetTrendingSeriesUseCase,
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val getAllSeriesUseCase: GetAllSeriesUseCase,
) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadAllData()
    }

    private fun loadAllData(genreId: Long? = null) {
        fetchPopularMedia(genreId)

        MediaContentType.entries.forEach {
            fetchSectionData(it)
        }

        loadGenres()
    }

    suspend fun getDataOfSection(
        sectionType: MediaContentType,
        genreId: Long? = null
    ): Pair<List<Movie>, List<Series>> {
        return when (sectionType) {
            MediaContentType.TOP_RATING -> {
                Pair(
                    getTopRatingMoviesUseCase.getTopRatingMovies(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                    getTopRatingSeriesUseCase.getTopRatingSeries(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                )
            }

            MediaContentType.TRENDING -> {
                Pair(
                    getTrendingMoviesUseCase.getTrendingMovies(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                    getTrendingSeriesUseCase.getTrendingSeries(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                )
            }

            MediaContentType.FREE_TO_WATCH -> {
                Pair(
                    getFreeToWatchMoviesUseCase.getFreeToWatchMovies(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                    emptyList()
                )
            }

            MediaContentType.UPCOMING -> {
                Pair(
                    getUpcomingMoviesUseCase.getUpcomingMovies(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                    emptyList()
                )
            }

            MediaContentType.NOW_PLAYING -> {
                Pair(
                    getNowPlayingMoviesUseCase.getNowPlayingMovies(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                    emptyList(),
                )
            }

            MediaContentType.MORE_RECOMMENDED -> {
                Pair(
                    getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                    getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                )
            }

            MediaContentType.AIRING_TODAY -> {
                Pair(
                    emptyList(),
                    getAiringTodaySeriesUseCase.getAiringTodaySeries(
                        page = 1,
                        categoryId = genreId?.toString()
                    ),
                )
            }

            MediaContentType.ON_TV -> {
                Pair(
                    emptyList(),
                    getOnTvSeriesUseCase.getOnTvSeries(page = 1, categoryId = genreId?.toString()),
                )
            }
        }
    }

    private fun fetchPopularMedia(genreId: Long? = null) {
        tryToCall(
            block = { fetchPopularMediaBlock(genreId) },
            onSuccess = ::onSuccessFetchPopularMedia,
            onError = ::handleError
        )
    }

    private suspend fun fetchPopularMediaBlock(genreId: Long? = null): Pair<List<Movie>, List<Series>> {
        val series = getPopularSeriesUseCase.getPopularSeries(
            page = 1,
            categoryId = genreId?.toString()
        )
        val movies = getPopularMoviesUseCase.getPopularMovies(
            page = 1,
            categoryId = genreId?.toString()
        )
        return Pair(movies, series)
    }

    private fun onSuccessFetchPopularMedia(moviesAndSeries: Pair<List<Movie>, List<Series>>) {
        updateState {
            it.copy(
                popularMovies = moviesAndSeries.first.map(Movie::toHomeMediaUiState),
                popularSeries = moviesAndSeries.second.map(Series::toHomeMediaUiState),
            )
        }
    }

    private fun loadGenres() {
        tryToCall(
            block = ::loadGenresBlock,
            onSuccess = { genres -> updateState { it.copy(genres = genres.toList()) } },
            onError = ::handleError
        )
    }

    private suspend fun loadGenresBlock(): List<HomeScreenState.GenreUiState> {
        val movieGenres = getMoviesGenresUseCase.getMoviesGenres()
        val seriesGenres = getSeriesGenresUseCase.getSeriesGenres()

        return buildSet {
            add(HomeScreenState.GenreUiState.defaultGenre)
            movieGenres.mapTo(this) { it.toHomeGenreUiState() }
            seriesGenres.mapTo(this) { it.toHomeGenreUiState() }
        }.toList()
    }

    override fun onClickProfile() {
        sendEffect(HomeEffect.NavigateToProfile)
    }

    override fun onClickMedia(mediaId: Long, isMovie: Boolean) {
        sendEffect(
            HomeEffect.NavigateMediaDetails(
                mediaId = mediaId,
                isMovie = isMovie
            )
        )
    }

    override fun onClickSeeAll(
        mediaContentType: MediaContentType,
        mediaType: MediaType
    ) {
        sendEffect(
            HomeEffect.NavigateToSeeAllScreen(
                mediaContentType,
                mediaType = mediaType
            )
        )
    }

    private fun fetchMediaByCategory(genreId: Long? = null) {
        tryToCall(
            block = {
                Pair(
                    getAllMoviesUseCase.getAllMovies(page = 1, categoryId = genreId?.toString()),
                    getAllSeriesUseCase.getAllSeries(page = 1, categoryId = genreId?.toString())
                )
            },
            onSuccess = { (movies, series) ->
                updateState {
                    it.copy(
                        categoriesMedia = movies.map(Movie::toHomeMediaUiState) + series.map(Series::toHomeMediaUiState)
                    )
                }
            },
            onError = ::handleError
        )
    }


    override fun onClickTab(tabIndex: Int) {

        if (tabIndex == HomeScreenState.Tab.CATEGORIES.ordinal) {
            fetchMediaByCategory()
        }

        updateState {
            it.copy(selectedTab = HomeScreenState.Tab.entries[tabIndex])
        }
    }

    override fun onGenreSelected(genreIndex: Int) {

        fetchMediaByCategory(screenState.value.genres[genreIndex].id)

        updateState {
            it.copy(
                selectedGenreIndex = genreIndex
            )
        }
    }

    override fun onSortingSelected(filter: HomeScreenState.SortingType) {
        updateState {
            it.copy(selectedSortingType = filter)
        }
        sortCategoriesMedia()
    }

    private fun sortCategoriesMedia() {
        if (screenState.value.selectedGenreIndex == 0) return
        when (screenState.value.selectedSortingType) {
            HomeScreenState.SortingType.ALL -> {} // TODO
            HomeScreenState.SortingType.POPULARITY -> {} // TODO
            HomeScreenState.SortingType.LATEST -> {} // TODO
        }
    }

    private fun fetchSectionData(
        sectionType: MediaContentType,
    ) {
        tryToCall(
            block = { getDataOfSection(sectionType) },
            onSuccess = { onSuccessFetchData(it, sectionType) },
            onError = ::handleError
        )
    }

    private fun onSuccessFetchData(
        moviesAndSeries: Pair<List<Movie>, List<Series>>,
        sectionType: MediaContentType
    ) {
        val section = HomeScreenState.SectionUiState(
            movies = moviesAndSeries.first.map(Movie::toHomeMediaUiState),
            series = moviesAndSeries.second.map(Series::toHomeMediaUiState)
        )
        updateState {
            it.copy(
                sections = (it.sections + (sectionType to section))
                    .entries.sortedBy { entry -> entry.key.ordinal }
                    .associate { entry -> entry.key to entry.value }
            )
        }
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