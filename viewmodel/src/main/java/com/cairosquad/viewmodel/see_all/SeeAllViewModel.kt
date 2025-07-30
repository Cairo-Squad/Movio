package com.cairosquad.viewmodel.see_all

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.see_all.SeeAllScreenState.GenreUiState.Companion.defaultGenre
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SeeAllViewModel(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val seeAllMoviesPager: SeeAllMoviesPager,
    private val seeAllSeriesPager: SeeAllSeriesPager
) : BaseViewModel<SeeAllScreenState, SeeAllEffect>(SeeAllScreenState()),
    SeeAllInteractionsListener {

    var contentType: MediaContentType = MediaContentType.TOP_RATING
    var mediaType: MediaType = MediaType.MOVIES

    override fun onClickBack() {
        sendEffect(SeeAllEffect.NavigateBack)
    }

    override fun onRefresh(genreIndex: Int) {
        val genreId = screenState.value.genres.getOrNull(genreIndex)?.id
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            loadData(contentType, mediaType, genreId = genreId)
            delay(500L)
            updateState { it.copy(isRefreshing = false) }
        }
    }

    fun updateScreenStatus(status: SeeAllScreenState.ScreenStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(errorStatus: ErrorStatus?) {
        updateState { it.copy(errorStatus = errorStatus) }
    }


    override fun onClickMedia(mediaId: Long, isMovie: Boolean) {
        sendEffect(SeeAllEffect.NavigateMediaDetails(mediaId, isMovie))
    }

    override fun onGenreSelected(genreIndex: Int) {
        if (genreIndex == screenState.value.selectedGenreIndex) return
        val genreId = screenState.value.genres.getOrNull(genreIndex)?.id
        updateState { it.copy(selectedGenreIndex = genreIndex) }
        loadData(contentType, mediaType, genreId = genreId)
    }

    fun loadData(
        contentType: MediaContentType,
        mediaType: MediaType,
        genreId: Long? = null
    ) {
        this.mediaType = mediaType
        this.contentType = contentType
        loadGenres()
        updateScreenStatus(SeeAllScreenState.ScreenStatus.LOADING)
        updateErrorStatus(null)
        tryToCall(
            onStart = { updateState { it.copy(screenStatus = SeeAllScreenState.ScreenStatus.LOADING) } },
            block = {
                val media = loadDataFlow(genreId)
                media
            },
            onSuccess = { media ->
                updateState {
                    it.copy(
                        mediaList = media,
                        screenStatus = SeeAllScreenState.ScreenStatus.SUCCESS
                    )
                }
            },
            onError = ::handleError,

            )
    }

    private fun loadDataFlow(genreId: Long?): Flow<PagingData<SeeAllScreenState.MediaUiState>> {
        val (moviesFlowGetter, seriesFlowGetter) = getDataPagerFetcher2(contentType, genreId)

        return when (mediaType) {
            MediaType.MOVIES -> {
                moviesFlowGetter().map { pagingData ->
                    pagingData.map { it.toSeeAllMediaUiState() }
                }
            }

            MediaType.SERIES -> {
                seriesFlowGetter().map { pagingData ->
                    pagingData.map { it.toSeeAllMediaUiState() }
                }
            }
        }
    }

    fun getDataPagerFetcher2(
        contentType: MediaContentType,
        genreId: Long?
    ): Pair<() -> Flow<PagingData<Movie>>, () -> Flow<PagingData<Series>>> {
        return when (contentType) {
            MediaContentType.TOP_RATING -> Pair(
                { seeAllMoviesPager.getTopRatingMovies(genreId) },
                { seeAllSeriesPager.getTopRatingSeries(genreId) }
            )

            MediaContentType.TRENDING -> Pair(
                { seeAllMoviesPager.getTrendingMovies(genreId) },
                { seeAllSeriesPager.getTrendingSeries(genreId) }
            )

            MediaContentType.FREE_TO_WATCH -> Pair(
                { seeAllMoviesPager.getFreeToWatchMovies(genreId) },
                { seeAllSeriesPager.getFreeToWatchSeries(genreId) }
            )

            MediaContentType.UPCOMING -> Pair(
                { seeAllMoviesPager.getUpcomingMovies(genreId) },
                { flowOf(PagingData.empty()) }
            )

            MediaContentType.NOW_PLAYING -> Pair(
                { seeAllMoviesPager.getNowPlayingMovies(genreId) },
                { flowOf(PagingData.empty()) }
            )

            MediaContentType.MORE_RECOMMENDED -> Pair(
                { seeAllMoviesPager.getMoreRecommendedMovies(genreId) },
                { seeAllSeriesPager.getMoreRecommendedSeries(genreId) }
            )

            MediaContentType.AIRING_TODAY -> Pair(
                { flowOf(PagingData.empty()) },
                { seeAllSeriesPager.getAiringTodaySeries(genreId) }
            )

            MediaContentType.ON_TV -> Pair(
                { flowOf(PagingData.empty()) },
                { seeAllSeriesPager.getOnTvSeries(genreId) }
            )
        }
    }

    fun getDataFetcher(
        contentType: MediaContentType
    ): Pair<suspend (Int, Long?) -> List<Movie>, suspend (Int, Long?) -> List<Series>> {
        return when (contentType) {
            MediaContentType.TOP_RATING -> Pair(
                { page, genreId ->
                    manageMoviesUseCase.getTopRatingMovies(
                        page,
                        genreId
                    )
                },
                { page, genreId ->
                    manageSeriesUseCase.getTopRatingSeries(
                        page,
                        genreId
                    )
                }
            )

            MediaContentType.TRENDING -> Pair(
                { page, genreId ->
                    manageMoviesUseCase.getTrendingMovies(
                        page,
                        genreId
                    )
                },
                { page, genreId ->
                    manageSeriesUseCase.getTrendingSeries(
                        page,
                        genreId
                    )
                }
            )

            MediaContentType.FREE_TO_WATCH -> Pair(
                { page, genreId ->
                    manageMoviesUseCase.getFreeToWatchMovies(
                        page,
                        genreId
                    )
                },
                { _, _ -> emptyList() }
            )

            MediaContentType.UPCOMING -> Pair(
                { page, genreId ->
                    manageMoviesUseCase.getUpcomingMovies(
                        page,
                        genreId
                    )
                },
                { _, _ -> emptyList() }
            )

            MediaContentType.NOW_PLAYING -> Pair(
                { page, genreId ->
                    manageMoviesUseCase.getNowPlayingMovies(
                        page,
                        genreId
                    )
                },
                { _, _ -> emptyList() }
            )

            MediaContentType.MORE_RECOMMENDED -> Pair(
                { page, genreId ->
                    manageMoviesUseCase.getMoreRecommendedMovies(
                        page,
                        genreId
                    )
                },
                { page, genreId ->
                    manageSeriesUseCase.getMoreRecommendedSeries(
                        page,
                        genreId
                    )
                }
            )

            MediaContentType.AIRING_TODAY -> Pair(
                { _, _ -> emptyList() },
                { page, genreId ->
                    manageSeriesUseCase.getAiringTodaySeries(
                        page,
                        genreId
                    )
                }
            )

            MediaContentType.ON_TV -> Pair(
                { _, _ -> emptyList() },
                { page, genreId -> manageSeriesUseCase.getOnTvSeries(page, genreId) }
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
            MediaType.MOVIES -> {
                val movieGenres = manageMoviesUseCase.getMoviesGenres()
                buildSet {
                    add(defaultGenre)
                    movieGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                }.toList()
            }

            MediaType.SERIES -> {
                val seriesGenres = manageSeriesUseCase.getSeriesGenres()
                buildSet {
                    add(defaultGenre)
                    seriesGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                }.toList()
            }
        }
    }

    fun handleError(e: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(e),
                screenStatus = SeeAllScreenState.ScreenStatus.FAILED,
            )
        }
    }

    fun handleHomeException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}
