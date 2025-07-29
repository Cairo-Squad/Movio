package com.cairosquad.viewmodel.see_all

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SeeAllViewModel(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase
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
        if (genreIndex == screenState.value.selectedGenreIndex) return
        val genreId = screenState.value.genres.getOrNull(genreIndex)?.id
        updateState { it.copy(selectedGenreIndex = genreIndex) }
        loadData(contentType, mediaType, page = 1, genreId = genreId)
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
                    add(SeeAllScreenState.GenreUiState.defaultGenre)
                    movieGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                }.toList()
            }

            MediaType.SERIES -> {
                val seriesGenres = manageSeriesUseCase.getSeriesGenres()
                buildSet {
                    add(SeeAllScreenState.GenreUiState.defaultGenre)
                    seriesGenres.mapTo(this) { it.toSeeAllGenreUiState() }
                }.toList()
            }

            MediaType.BOTH -> {
                val movieGenres = manageMoviesUseCase.getMoviesGenres()
                val seriesGenres = manageSeriesUseCase.getSeriesGenres()
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
                screenStatus = SeeAllScreenState.ScreenStatus.FAILED,
                isLoading = false
            )
        }
    }

    private fun handleHomeException(e: Throwable): ErrorStatus {
        return if (e is MovioException) exceptionToErrorStatus(e)
        else ErrorStatus.UNKNOWN_ERROR
    }
}
