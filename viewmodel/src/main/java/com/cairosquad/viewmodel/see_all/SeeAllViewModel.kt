package com.cairosquad.viewmodel.see_all

import android.util.Log
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
    private val getTrendingSeriesUseCase: GetTrendingSeriesUseCase,
) : BaseViewModel<SeeAllScreenState, SeeAllEffect>(initialState = SeeAllScreenState()),
    SeeAllInteractionsListener {

    var contentType: MediaContentType = MediaContentType.TOP_RATING
    var mediaType: MediaType = MediaType.MOVIES

    init {
        loadGenres()
    }

    override fun onGenreSelected(genreIndex: Int) {

        val genreId = screenState.value.genres.getOrNull(genreIndex)?.id

        Log.d("asdasd", "onGenreSelected: $genreId")

        loadData(
            contentType = contentType,
            mediaType = mediaType,
            page = 1,
            genreId = genreId
        )

        updateState {
            it.copy(selectedGenreIndex = genreIndex)
        }
    }

    override fun onClickMedia(mediaId: Long, isMovie: Boolean) {
        sendEffect(
            SeeAllEffect.NavigateMediaDetails(
                mediaId = mediaId,
                isMovie = isMovie
            )
        )
    }

    override fun onClickBack() {
        sendEffect(SeeAllEffect.NavigateBack)
    }

    fun loadData(
        contentType: MediaContentType,
        mediaType: MediaType,
        page: Int = 1,
        genreId: Long? = null
    ) {
        this.mediaType = mediaType
        this.contentType = contentType

        val (moviesFetcher, seriesFetcher) = getDataFetcher(contentType)

        tryToCall(
            block = {
                when (mediaType) {
                    MediaType.MOVIES -> {
                        moviesFetcher(page, genreId).map(Movie::toSeeAllMediaUiState)
                    }
                    MediaType.SERIES -> {
                        seriesFetcher(page, genreId).map(Series::toSeeAllMediaUiState)
                    }
                    MediaType.BOTH -> {
                        combineTwoList(
                            moviesFetcher(page, genreId).map(Movie::toSeeAllMediaUiState),
                            seriesFetcher(page, genreId).map(Series::toSeeAllMediaUiState)
                        )
                    }
                }
            },
            onSuccess = { result -> updateState { it.copy(mediaList = result) } },
            onError = ::handleError
        )
    }

    private fun <T> combineTwoList(
        list1: List<T>,
        list2: List<T>,
    ): List<T> {
        val mergedList = mutableListOf<T>()
        val iterator1 = list1.iterator()
        val iterator2 = list2.iterator()

        while (iterator1.hasNext() || iterator2.hasNext()) {
            if (iterator1.hasNext()) mergedList.add(iterator1.next())
            if (iterator2.hasNext()) mergedList.add(iterator2.next())
        }
        return mergedList
    }


    fun getDataFetcher(
        mediaContentType: MediaContentType,
    ): Pair<
            suspend (page: Int, genreId: Long?) -> List<Movie>,
            suspend (page: Int, genreId: Long?) -> List<Series>
        > {

        return when (mediaContentType) {
            MediaContentType.TOP_RATING -> Pair(
                { page, genreId ->
                    getTopRatingMoviesUseCase.getTopRatingMovies(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                },
                { page, genreId ->
                    getTopRatingSeriesUseCase.getTopRatingSeries(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                }
            )

            MediaContentType.TRENDING -> Pair(
                { page, genreId ->
                    getTrendingMoviesUseCase.getTrendingMovies(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                },
                { page, genreId ->
                    getTrendingSeriesUseCase.getTrendingSeries(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                }
            )
            MediaContentType.FREE_TO_WATCH -> Pair(
                { page, genreId ->
                    getFreeToWatchMoviesUseCase.getFreeToWatchMovies(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                },
                { _, _ ->
                    emptyList()
                }
            )
            MediaContentType.UPCOMING -> Pair(
                { page, genreId ->
                    getUpcomingMoviesUseCase.getUpcomingMovies(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                },
                { _, _ ->
                    emptyList()
                }
            )
            MediaContentType.NOW_PLAYING -> Pair(
                { page, genreId ->
                    getNowPlayingMoviesUseCase.getNowPlayingMovies(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                },
                { _, _ ->
                    emptyList()
                }
            )
            MediaContentType.MORE_RECOMMENDED -> Pair(
                { page, genreId ->
                    getMoreRecommendedMoviesUseCase.getMoreRecommendedMovies(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                },
                { page, genreId ->
                    getMoreRecommendedSeriesUseCase.getMoreRecommendedSeries(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                }
            )
            MediaContentType.AIRING_TODAY -> Pair(
                { _, _ ->
                    emptyList()
                },
                { page, genreId ->
                    getAiringTodaySeriesUseCase.getAiringTodaySeries(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                }
            )
            MediaContentType.ON_TV -> Pair(
                { _, _ ->
                    emptyList()
                },
                { page, genreId ->
                    getOnTvSeriesUseCase.getOnTvSeries(
                        page = page,
                        categoryId = genreId?.toString()
                    )
                }
            )
        }
    }


    private fun loadGenres() {
        tryToCall(
            block = {
                val movieGenres = getMoviesGenresUseCase.getMoviesGenres()
                val seriesGenres = getSeriesGenresUseCase.getSeriesGenres()

                val combined = buildSet {
                    add(SeeAllScreenState.GenreUiState.defaultGenre)
                    movieGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                    seriesGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                }

                combined
            },
            onSuccess = { genres ->
                updateState { it.copy(genres = genres.toList()) }
            },
            onError = ::handleError
        )
    }

    private fun handleError(throwable: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(throwable),
                screenStatus = SeeAllScreenState.ScreenStatus.FAILED
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