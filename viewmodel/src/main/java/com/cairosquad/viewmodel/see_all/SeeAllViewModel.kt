package com.cairosquad.viewmodel.see_all

import com.cairosquad.domain.exception.MovioException
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
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

class SeeAllViewModel(
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
    private val getMoviesGenresUseCase: GetMoviesGenresUseCase,
    private val getSeriesGenresUseCase: GetSeriesGenresUseCase,
    private val getTrendingSeriesUseCase: GetTrendingSeriesUseCase
) : BaseViewModel<SeeAllScreenState, SeeAllEffect>(SeeAllScreenState()),
    SeeAllInteractionsListener {

    var contentType: MediaContentType = MediaContentType.TOP_RATING
    var mediaType: MediaType = MediaType.MOVIES

    override fun onClickBack() {
        sendEffect(SeeAllEffect.NavigateBack)
    }

    override fun onClickMedia(mediaId: Long, isMovie: Boolean) {
        sendEffect(SeeAllEffect.NavigateMediaDetails(mediaId, isMovie))
    }

    override fun onGenreSelected(genreIndex: Int) {
        val genreId = screenState.value.genres.getOrNull(genreIndex)?.id
        loadData(contentType, mediaType, page = 1, genreId = genreId)
        updateState { it.copy(selectedGenreIndex = genreIndex) }
    }

    fun loadData(
        contentType: MediaContentType,
        mediaType: MediaType,
        page: Int = 1,
        genreId: Long? = null
    ) {
        this.mediaType = mediaType
        this.contentType = contentType
        loadGenres()
        tryToCall(
            onStart = {
                updateState {
                    it.copy(
                        screenStatus = SeeAllScreenState.ScreenStatus.LOADING,
                        isLoading = true
                    )
                }
            },
            block = { loadDataBlock(page, genreId) },
            onSuccess = { result ->
                updateState {
                    it.copy(
                        mediaList = result,
                        screenStatus = SeeAllScreenState.ScreenStatus.SUCCESS,
                        isLoading = false
                    )
                }
            },
            onError = ::handleError
        )
    }

    private suspend fun loadDataBlock(
        page: Int = 1,
        genreId: Long? = null
    ): List<SeeAllScreenState.MediaUiState> {
        val (moviesFetcher, seriesFetcher) = getDataFetcher(contentType)

        return when (mediaType) {
            MediaType.MOVIES -> moviesFetcher(page, genreId).map(Movie::toSeeAllMediaUiState)
            MediaType.SERIES -> seriesFetcher(page, genreId).map(Series::toSeeAllMediaUiState)
            MediaType.BOTH -> combineTwoList(
                moviesFetcher(page, genreId).map(Movie::toSeeAllMediaUiState),
                seriesFetcher(page, genreId).map(Series::toSeeAllMediaUiState)
            )
        }
    }

    private fun <T> combineTwoList(list1: List<T>, list2: List<T>): List<T> {
        val mergedList = mutableListOf<T>()
        val i1 = list1.iterator()
        val i2 = list2.iterator()
        while (i1.hasNext() || i2.hasNext()) {
            if (i1.hasNext()) mergedList.add(i1.next())
            if (i2.hasNext()) mergedList.add(i2.next())
        }
        return mergedList
    }

    fun getDataFetcher(
        contentType: MediaContentType
    ): Pair<suspend (Int, Long?) -> List<Movie>, suspend (Int, Long?) -> List<Series>> {
        return when (contentType) {
            MediaContentType.TOP_RATING -> Pair(
                { page, genreId ->
                    getTopRatingMoviesUseCase.getTopRatingMovies(
                        page,
                        genreId?.toString()
                    )
                },
                { page, genreId ->
                    getTopRatingSeriesUseCase.getTopRatingSeries(
                        page,
                        genreId?.toString()
                    )
                }
            )

            MediaContentType.TRENDING -> Pair(
                { page, genreId ->
                    getTrendingMoviesUseCase.getTrendingMovies(
                        page,
                        genreId?.toString()
                    )
                },
                { page, genreId ->
                    getTrendingSeriesUseCase.getTrendingSeries(
                        page,
                        genreId?.toString()
                    )
                }
            )

            MediaContentType.FREE_TO_WATCH -> Pair(
                { page, genreId ->
                    getFreeToWatchMoviesUseCase.getFreeToWatchMovies(
                        page,
                        genreId?.toString()
                    )
                },
                { _, _ -> emptyList() }
            )

            MediaContentType.UPCOMING -> Pair(
                { page, genreId ->
                    getUpcomingMoviesUseCase.getUpcomingMovies(
                        page,
                        genreId?.toString()
                    )
                },
                { _, _ -> emptyList() }
            )

            MediaContentType.NOW_PLAYING -> Pair(
                { page, genreId ->
                    getNowPlayingMoviesUseCase.getNowPlayingMovies(
                        page,
                        genreId?.toString()
                    )
                },
                { _, _ -> emptyList() }
            )

            MediaContentType.MORE_RECOMMENDED -> Pair(
                { page, genreId ->
                    getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(
                        page,
                        genreId?.toString()
                    )
                },
                { page, genreId ->
                    getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(
                        page,
                        genreId?.toString()
                    )
                }
            )

            MediaContentType.AIRING_TODAY -> Pair(
                { _, _ -> emptyList() },
                { page, genreId ->
                    getAiringTodaySeriesUseCase.getAiringTodaySeries(
                        page,
                        genreId?.toString()
                    )
                }
            )

            MediaContentType.ON_TV -> Pair(
                { _, _ -> emptyList() },
                { page, genreId -> getOnTvSeriesUseCase.getOnTvSeries(page, genreId?.toString()) }
            )
        }
    }

    fun loadGenres() {
        tryToCall(
            block = ::loadGenresBlock,
            onSuccess = { genres -> updateState { it.copy(genres = genres) } },
            onError = ::handleError
        )
    }

    private suspend fun loadGenresBlock(): List<SeeAllScreenState.GenreUiState> {
        return when (mediaType) {
            MediaType.MOVIES -> getMoviesGenresUseCase.getMoviesGenres()
                .map(Genre::toSeeAllGenreUiState)

            MediaType.SERIES -> getSeriesGenresUseCase.getSeriesGenres()
                .map(Genre::toSeeAllGenreUiState)

            MediaType.BOTH -> {
                val movieGenres = getMoviesGenresUseCase.getMoviesGenres()
                val seriesGenres = getSeriesGenresUseCase.getSeriesGenres()
                buildSet {
                    add(SeeAllScreenState.GenreUiState.defaultGenre)
                    movieGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                    seriesGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                }.toList()
            }
        }
    }

    fun handleError(e: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(e),
                screenStatus = SeeAllScreenState.ScreenStatus.FAILED
            )
        }
    }

    private fun handleHomeException(e: Throwable): ErrorStatus {
        return if (e is MovioException) exceptionToErrorStatus(e)
        else ErrorStatus.UNKNOWN_ERROR
    }
}
