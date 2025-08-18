package com.cairosquad.viewmodel.details.series

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.R
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
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SeriesDetailsViewModel.Factory::class)
class SeriesDetailsViewModel @AssistedInject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val loginUseCase: LoginUseCase,
    private val accountUseCase: AccountUseCase,
    private val ratedItemsUseCase: GetRatedItemsUseCase,
    @Assisted private val seriesId: Long,
) : BaseViewModel<SeriesDetailsScreenState, SeriesDetailEffect>(SeriesDetailsScreenState()),
    SeriesDetailsInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(seriesId: Long): SeriesDetailsViewModel
    }

    init {
        fetchDetailsData()
        addSeriesToHistory()
    }

    fun fetchDetailsData() {
        getSeriesDetails()
        getTopCast()
        getSeasons()
        getReviews()
        getSimilarSeries()
    }

    private fun getSeriesInFavorite() {
        checkUserLoggedIn {
            loadFavoriteSeries()
        }
    }

    private fun getSeriesInRated() {
        checkUserLoggedIn {
            loadRatedSeries()
        }
    }

    private fun loadRatedSeries() {
        tryToCall(
            block = { ratedItemsUseCase.getRatedSeries(1) },
            onSuccess = ::onLoadRatedSeriesSuccess,
            onError = ::onLoadRatedSeriesError
        )
    }

    private fun onLoadRatedSeriesSuccess(ratedItems: List<Pair<Series, Double>>) {
        val matchedRating = ratedItems
            .firstOrNull { (series, _) -> series.id == seriesId }
            ?.second

        updateState { state ->
            state.copy(
                isRated = matchedRating != null,
                userRating = matchedRating?.let { (it / 2).toInt() } ?: state.userRating
            )
        }
    }

    private fun onLoadRatedSeriesError(throwable: Throwable) {

    }

    private fun checkUserLoggedIn(onLoggedIn: () -> Unit) {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = {
                if (it) onLoggedIn()
                else updateState { it.copy(showLoginBottomSheet = true) }
            },
            onError = {}
        )
    }

    private fun loadFavoriteSeries() {
        tryToCall(
            block = { accountUseCase.getFavoriteSeries(1) },
            onSuccess = ::onLoadFavoriteSeriesSuccess,
            onError = {}
        )
    }

    private fun onLoadFavoriteSeriesSuccess(series: List<Series>) {
        updateState { it.copy(isFavorite = series.any { _series -> _series.id == seriesId }) }
    }

    private fun addSeriesToHistory() {
        tryToCall(
            block = { accountUseCase.addSeriesToHistory(seriesId) },
            onSuccess = {},
            onError = {}
        )
    }

    override fun onBackClick() {
        sendEffect(SeriesDetailEffect.NavigateBack)
    }

    override fun onShareClick() {
        updateState { it.copy(showShareBottomSheet = true) }
    }

    override fun onFavoriteClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = ::onFavoriteClickedSuccess,
            onError = {}
        )
    }

    private fun onFavoriteClickedSuccess(isAuthed: Boolean) {
        if (!isAuthed) {
            updateState { it.copy(showLoginBottomSheet = true) }
        } else {
            if (screenState.value.isFavorite) {
                removeFromFavorite(seriesId)
            } else {
                addToFavorite(seriesId)
            }
        }
    }

    private fun removeFromFavorite(seriesId: Long) {
        tryToCall(
            block = { accountUseCase.removeSeriesFromFavorite(seriesId) },
            onSuccess = { onRemoveFromFavoriteSuccess() },
            onError = ::onRemoveFromFavoriteError
        )
    }

    private fun onRemoveFromFavoriteSuccess() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    showSnackBar = true, isProcessSuccess = true,
                    snackMessageId = R.string.series_favorite_remove_success,
                    isFavorite = false
                )
            }
            delay(2000)
            updateState { it.copy(showSnackBar = false) }
        }
    }

    private fun onRemoveFromFavoriteError(throwable: Throwable) {
        showSnackBar(R.string.series_favorite_remove_fail, false)
    }

    private fun addToFavorite(seriesId: Long) {
        tryToCall(
            block = { accountUseCase.addSeriesToFavorite(seriesId) },
            onSuccess = { onAddToFavoriteSuccess() },
            onError = { onAddToFavoriteError() }
        )
    }

    private fun onAddToFavoriteSuccess() {
        updateState { it.copy(isFavorite = true) }
        showSnackBar(messageId = R.string.series_favorite_success, isSuccessful = true)
    }

    private fun onAddToFavoriteError() {
        showSnackBar(messageId = R.string.series_favorite_fail, isSuccessful = false)
    }

    override fun onRateClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = ::onRateClickedSuccess,
            onError = {}
        )
    }

    private fun onRateClickedSuccess(authed: Boolean) {
        if (!authed) {
            updateState { it.copy(showLoginBottomSheet = true) }
        } else {
            if (screenState.value.isRated)
                updateState { it.copy(showSuccessRatedBottomSheet = true) }
            else
                updateState { it.copy(showRateBottomSheet = true) }
        }
    }


    override fun onPlayTrailerClick() {
        sendEffect(SeriesDetailEffect.PlayTrailer)
    }

    override fun onAddToListClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy( showLoginBottomSheet = true) }
                } else {
                    updateState {
                        it.copy(
                            showSnackBar = true,
                            isProcessSuccess = false,
                            snackMessageId = R.string.lists_is_not_supported_for_series
                        )
                    }
                    viewModelScope.launch {
                        delay(2000)
                        updateState { it.copy(showSnackBar = false) }
                    }
                }
            },
            onError = {}
        )
    }


    private fun loadSeriesLists() {
        tryToCall(
            block = { accountUseCase.getSeriesLists(1) },
            onSuccess = ::onLoadSeriesListsSuccess,
            onError = {}
        )
    }

    private fun onLoadSeriesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            it.copy(
                showAddToListBottomSheet = true,
                seriesLists = mediaLists.map { list -> list.toUiState() })
        }
    }

    override fun onCreateListClick() {
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

    override fun onDismissSuccessRatedBottomSheet() {
        updateState { it.copy(showSuccessRatedBottomSheet = false) }
    }

    override fun onValueChange(listName: String) {
        updateState { it.copy(newListName = listName) }
    }

    override fun onRateChange(rate: Int) {
        updateState { it.copy(rating = rate) }
    }

    override fun onSubmitRateClick(rate: Int) {
        tryToCall(
            block = { manageSeriesUseCase.addSeriesRating(seriesId, rate.toFloat() * 2) },
            onSuccess = { onSubmitRateClickedSuccess(rate) },
            onError = ::onSubmitRateClickedError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onSubmitRateClickedSuccess(rating: Int) {
        updateState {
            it.copy(
                showRateBottomSheet = false,
                showSuccessRatedBottomSheet = true,
                isRated = true,
                userRating = rating
            )
        }
    }

    private fun onSubmitRateClickedError(throwable: Throwable) {
        showSnackBar(R.string.rated_failed, false)
    }

    override fun onCopy(messageId: Int, isSuccessful: Boolean) {
        onDismissShareBottomSheet()
        viewModelScope.launch {
            delay(500)
            showSnackBar(messageId, isSuccessful)
        }
    }

    fun showSnackBar(messageId: Int, isSuccessful: Boolean, durationMillis: Long = 2000) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    showSnackBar = true,
                    snackMessageId = messageId,
                    isProcessSuccess = isSuccessful
                )
            }
            delay(durationMillis)
            updateState { it.copy(showSnackBar = false) }
        }
    }

    override fun onArtistClick(artistId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToArtistDetails(artistId))
    }

    override fun onSeeAllArtistsClick(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllArtists(seriesId))
    }

    override fun onSeasonClick(seriesId: Long, seasonNumber: Int) {
        sendEffect(
            SeriesDetailEffect.NavigateToSeasonDetails(
                seriesId = seriesId,
                seasonNumber = seasonNumber
            )
        )
    }

    override fun onSeeAllSeasonsClick(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllSeasons(seriesId))
    }

    override fun onSeeAllReviewsClick(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllReviews(seriesId))
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onSeeAllSimilarClick(seriesId: Long) {
        sendEffect(SeriesDetailEffect.NavigateToAllSimilar(seriesId))
    }

    override fun onNavigateToLogin() {
        sendEffect(SeriesDetailEffect.NavigateToLogin)
    }

    override fun onRefresh() {
        fetchDetailsData()
    }

    private fun getSeriesDetails() {
        tryToCall(
            onStart = ::onGetSeriesDetailsStart,
            block = { manageSeriesUseCase.getSeriesById(seriesId) },
            onSuccess = ::onGetSeriesDetailsSuccess,
            onError = ::onGetSeriesDetailsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetSeriesDetailsStart() {
        updateState { it.copy(basicDetailsSectionState = SectionStatus.LOADING) }
    }

    private fun onGetSeriesDetailsError(throwable: Throwable) {
        setError(throwable) { copy(basicDetailsSectionState = SectionStatus.ERROR) }
    }

    private fun onGetSeriesDetailsSuccess(series: Series) {
        updateState {
            it.copy(
                basicDetailsSectionState = SectionStatus.SUCCESS,
                series = series.toUiState()
            )
        }
    }

    private fun getTopCast() {
        tryToCall(
            onStart = ::onGetTopCastStart,
            block = { manageSeriesUseCase.getSeriesTopCast(seriesId, 1) },
            onSuccess = ::onGetTopCastSuccess,
            onError = ::onGetTopCastError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetTopCastStart() {
        updateState { it.copy(castSectionState = SectionStatus.LOADING) }
    }

    private fun onGetTopCastSuccess(cast: List<Artist>) {
        updateState {
            it.copy(
                castSectionState = SectionStatus.SUCCESS,
                cast = cast.map { it.toUiState() }
            )
        }
    }

    private fun onGetTopCastError(throwable: Throwable) {
        setError(throwable) { copy(castSectionState = SectionStatus.ERROR) }
    }

    private fun getSeasons() {
        tryToCall(
            onStart = ::onGetSeasonsStart,
            block = { manageSeriesUseCase.getSeriesSeasons(seriesId) },
            onSuccess = ::onGetSeasonsSuccess,
            onError = ::onGetSeasonsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetSeasonsStart() {
        updateState { it.copy(seasonsSectionState = SectionStatus.LOADING) }
    }

    private fun onGetSeasonsSuccess(seasons: List<Season>) {
        updateState {
            it.copy(
                seasonsSectionState = SectionStatus.SUCCESS,
                seasons = seasons.map { it.toUiState() }
            )
        }
    }

    private fun onGetSeasonsError(throwable: Throwable) {
        setError(throwable) { copy(seasonsSectionState = SectionStatus.ERROR) }
    }

    private fun getReviews() {
        tryToCall(
            onStart = ::onGetReviewsStart,
            block = { manageSeriesUseCase.getSeriesReviews(seriesId, 1) },
            onSuccess = ::onGetReviewsSuccess,
            onError = ::onGetReviewsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetReviewsStart() {
        updateState { it.copy(reviewsSectionState = SectionStatus.LOADING) }
    }

    private fun onGetReviewsSuccess(reviews: List<Review>) {
        updateState {
            it.copy(
                reviewsSectionState = SectionStatus.SUCCESS,
                reviews = reviews.map { it.toUiState() }
            )
        }
    }

    private fun onGetReviewsError(throwable: Throwable) {
        setError(throwable) { copy(reviewsSectionState = SectionStatus.ERROR) }
    }

    private fun getSimilarSeries() {
        tryToCall(
            onStart = ::onGetSimilarSeriesStart,
            block = { manageSeriesUseCase.getSimilarSeries(seriesId, 1) },
            onSuccess = ::onGetSimilarSeriesSuccess,
            onError = ::onGetSimilarSeriesError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetSimilarSeriesStart() {
        updateState { it.copy(similarSeriesSectionState = SectionStatus.LOADING) }
    }

    private fun onGetSimilarSeriesSuccess(similarSeries: List<Series>) {
        updateState {
            it.copy(
                similarSeriesSectionState = SectionStatus.SUCCESS,
                similarSeries = similarSeries.map { it.toUiState() }
            )
        }
    }

    private fun onGetSimilarSeriesError(throwable: Throwable) {
        setError(throwable) { copy(similarSeriesSectionState = SectionStatus.ERROR) }
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