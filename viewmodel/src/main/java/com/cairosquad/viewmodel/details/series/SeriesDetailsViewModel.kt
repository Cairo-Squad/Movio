package com.cairosquad.viewmodel.details.series

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SeriesDetailsViewModel(
    private val seriesDetailsUseCase: GetSeriesDetailsUseCase,
    seriesId: Long
) : BaseViewModel<SeriesDetailsScreenState, SeriesDetailEffect>(SeriesDetailsScreenState()),
    SeriesDetailsInteractionListener {

    init {
        loadDetails(seriesId)
    }

    fun loadDetails(seriesId: Long) {
        getSeriesDetails(seriesId)
        getTopCast(seriesId)
        getSeasons(seriesId)
        getReviews(seriesId)
        getSimilarSeries(seriesId)
    }

    override fun onBackClicked() {
        sendEffect(SeriesDetailEffect.NavigateBack)
    }

    override fun onShareClicked() {
        updateState { it.copy(showShareBottomSheet = true) }
    }

    override fun onFavoriteClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onRateClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onPlayTrailerClicked() {
        sendEffect(SeriesDetailEffect.PlayTrailer)
    }

    override fun onAddToListClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onDismissShareBottomSheet() {
        updateState { it.copy(showShareBottomSheet = false) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onCopy(message: String, isSuccessful: Boolean) {
        tryToCall(
            onStart = {
                onDismissShareBottomSheet()
                delay(500)
                updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessage = message,
                        isProcessSuccess = isSuccessful
                    )
                }
            },
            block = { delay(2000) },
            onSuccess = { updateState { it.copy(showSnackBar = false, snackMessage = message) } },
            onError = {},
        )
    }

    override fun onArtistClicked(artistId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToArtistDetails(artistId))
    }

    override fun onSeeAllArtistsClicked(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllArtists(seriesId))
    }

    override fun onSeasonClicked(seriesId: Long, seasonNumber: Int) {
        sendEffect(
            SeriesDetailEffect.NavigateToSeasonDetails(
                seriesId = seriesId,
                seasonNumber = seasonNumber
            )
        )
    }

    override fun onSeeAllSeasonsClicked(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllSeasons(seriesId))
    }

    override fun onSeeAllReviewsClicked(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllReviews(seriesId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onSeeAllSimilarClicked(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllSimilar(seriesId))
    }

    private fun getSeriesDetails(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(basicDetailsSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getSeries(seriesId) },
            onSuccess = ::setBasicSeriesDetailsToUiState,
            onError = { throwable ->
                setError(throwable) { copy(basicDetailsSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setBasicSeriesDetailsToUiState(series: Series) {
        updateState {
            it.copy(
                basicDetailsSectionState = ScreenStatus.SUCCESS,
                series = series.toUiState()
            )
        }
    }

    private fun getTopCast(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(castSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getSeriesTopCast(seriesId) },
            onSuccess = ::setTopCastToUiState,
            onError = { throwable ->
                setError(throwable) { copy(castSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setTopCastToUiState(cast: List<Artist>) {
        updateState {
            it.copy(
                castSectionState = ScreenStatus.SUCCESS,
                cast = cast.map { it.toUiState() }
            )
        }
    }

    private fun getSeasons(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(seasonsSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getSeriesSeasons(seriesId) },
            onSuccess = ::setSeasonToUiState,
            onError = { throwable ->
                setError(throwable) { copy(seasonsSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setSeasonToUiState(seasons: List<Season>) {
        updateState {
            it.copy(
                seasonsSectionState = ScreenStatus.SUCCESS,
                seasons = seasons.map { it.toUiState() }.reversed()
            )
        }
    }

    private fun getReviews(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(reviewsSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getSeriesReviews(seriesId) },
            onSuccess = ::setReviewsToUiState,
            onError = { throwable ->
                setError(throwable) { copy(reviewsSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setReviewsToUiState(reviews: List<Review>) {
        updateState {
            it.copy(
                reviewsSectionState = ScreenStatus.SUCCESS,
                reviews = reviews.map { it.toUiState() }
            )
        }
    }

    private fun getSimilarSeries(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(similarSeriesSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getSimilarSeries(seriesId) },
            onSuccess = ::setSimilarSeriesToUiState,
            onError = { throwable ->
                setError(throwable) { copy(similarSeriesSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setSimilarSeriesToUiState(similarSeries: List<Series>) {
        updateState {
            it.copy(
                similarSeriesSectionState = ScreenStatus.SUCCESS,
                similarSeries = similarSeries.map { it.toUiState() }
            )
        }
    }

    private fun setError(
        throwable: Throwable,
        updateSection: SeriesDetailsScreenState.() -> SeriesDetailsScreenState
    ) {
        updateState {
            it.updateSection().copy(
                errorStatus = handleDetailsException(throwable)
            )
        }
    }

    fun handleDetailsException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}