package com.cairosquad.viewmodel.details.series

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState.SectionStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@HiltViewModel(assistedFactory = SeriesDetailsViewModel.Factory::class)
class SeriesDetailsViewModel @AssistedInject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val loginUseCase: LoginUseCase,
    private val accountUseCase: AccountUseCase,
    @Assisted private val seriesId: Long,
) : BaseViewModel<SeriesDetailsScreenState, SeriesDetailEffect>(SeriesDetailsScreenState()),
    SeriesDetailsInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(seriesId: Long): SeriesDetailsViewModel
    }

    init {
        loadDetails(seriesId)
        addSeriesToHistory(seriesId)
    }

    fun loadDetails(seriesId: Long) {
        getSeriesDetails(seriesId)
        getTopCast(seriesId)
        getSeasons(seriesId)
        getReviews(seriesId)
        getSimilarSeries(seriesId)
    }

    private fun addSeriesToHistory(seriesId: Long) {
        tryToCall(
            block = { accountUseCase.addSeriesToHistory(seriesId) },
            onSuccess = {},
            onError = {}
        )
    }

    override fun onBackClicked() {
        sendEffect(SeriesDetailEffect.NavigateBack)
    }

    override fun onShareClicked() {
        updateState { it.copy(showShareBottomSheet = true) }
    }

    override fun onFavoriteClicked() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(showLoginBottomSheet = true) }
                } else {

                }
            },
            onError = {}
        )
    }

    override fun onRateClicked() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(showLoginBottomSheet = true) }
                } else {
                    updateState { it.copy(showRateBottomSheet = true) }
                }
            },
            onError = {}
        )
    }

    override fun onPlayTrailerClicked() {
        sendEffect(SeriesDetailEffect.PlayTrailer)
    }

    override fun onAddToListClicked() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(showLoginBottomSheet = true) }
                } else {
                    loadSeriesLists()
                }
            },
            onError = {}
        )
    }

    private fun loadSeriesLists() {
        tryToCall(
            block = accountUseCase::getSeriesLists,
            onSuccess = { mediaLists ->
                updateState {
                    it.copy(
                        showAddToListBottomSheet = true,
                        seriesLists = mediaLists.map { list -> list.toUiState() })
                }
            },
            onError = {}
        )
    }

    override fun onCreateListClicked() {
        updateState { it.copy(showAddToListBottomSheet = false, showCreateListBottomSheet = true) }
    }


    override fun onDismissShareBottomSheet() {
        updateState { it.copy(showShareBottomSheet = false) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(showRateBottomSheet = false) }
    }

    override fun onDismissAddToListBottomSheet() {
        updateState { it.copy(showAddToListBottomSheet = false) }
    }

    override fun onDismissCreateListBottomSheet() {
        updateState { it.copy(showCreateListBottomSheet = false) }
    }

    override fun onValueChange(listName: String) {
        updateState { it.copy(newListName = listName) }
    }

    override fun onRateChange(rate: Int) {
        updateState { it.copy(rating = rate) }
    }

    override fun onSubmitRateClicked(rate: Int) {
        updateState { it.copy(showRateBottomSheet = false) }
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
            onSuccess = {
                updateState {
                    it.copy(
                        showSnackBar = false,
                        snackMessage = message
                    )
                }
            },
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

    override fun onNavigateToLogin() {
        sendEffect(SeriesDetailEffect.NavigateToLogin)
    }

    override fun onRefresh() {
        loadDetails(seriesId)
    }

    private fun getSeriesDetails(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(basicDetailsSectionState = SectionStatus.LOADING) }
            },
            block = { manageSeriesUseCase.getSeriesById(seriesId) },
            onSuccess = ::setBasicSeriesDetailsToUiState,
            onError = { throwable ->
                setError(throwable) { copy(basicDetailsSectionState = SectionStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setBasicSeriesDetailsToUiState(series: Series) {
        updateState {
            it.copy(
                basicDetailsSectionState = SectionStatus.SUCCESS,
                series = series.toUiState()
            )
        }
    }

    private fun getTopCast(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(castSectionState = SectionStatus.LOADING) }
            },
            block = { manageSeriesUseCase.getSeriesTopCast(seriesId, 1) },
            onSuccess = ::setTopCastToUiState,
            onError = { throwable ->
                setError(throwable) { copy(castSectionState = SectionStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setTopCastToUiState(cast: List<Artist>) {
        updateState {
            it.copy(
                castSectionState = SectionStatus.SUCCESS,
                cast = cast.map { it.toUiState() }
            )
        }
    }

    private fun getSeasons(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(seasonsSectionState = SectionStatus.LOADING) }
            },
            block = { manageSeriesUseCase.getSeriesSeasons(seriesId) },
            onSuccess = ::setSeasonToUiState,
            onError = { throwable ->
                setError(throwable) { copy(seasonsSectionState = SectionStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setSeasonToUiState(seasons: List<Season>) {
        updateState {
            it.copy(
                seasonsSectionState = SectionStatus.SUCCESS,
                seasons = seasons.map { it.toUiState() }
            )
        }
    }

    private fun getReviews(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(reviewsSectionState = SectionStatus.LOADING) }
            },
            block = { manageSeriesUseCase.getSeriesReviews(seriesId, 1) },
            onSuccess = ::setReviewsToUiState,
            onError = { throwable ->
                setError(throwable) { copy(reviewsSectionState = SectionStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setReviewsToUiState(reviews: List<Review>) {
        updateState {
            it.copy(
                reviewsSectionState = SectionStatus.SUCCESS,
                reviews = reviews.map { it.toUiState() }
            )
        }
    }

    private fun getSimilarSeries(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(similarSeriesSectionState = SectionStatus.LOADING) }
            },
            block = { manageSeriesUseCase.getSimilarSeries(seriesId, 1) },
            onSuccess = ::setSimilarSeriesToUiState,
            onError = { throwable ->
                setError(throwable) { copy(similarSeriesSectionState = SectionStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setSimilarSeriesToUiState(similarSeries: List<Series>) {
        updateState {
            it.copy(
                similarSeriesSectionState = SectionStatus.SUCCESS,
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