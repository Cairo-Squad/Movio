package com.cairosquad.viewmodel.details.series.season

import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SeasonsViewModel @Inject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SeasonDetailsScreenState, SeasonDetailEffect>(SeasonDetailsScreenState()),
    SeasonDetailsInteractionListener {

    private val seriesId: Long = 0 // TODO: get from savedHandle

    init {
        loadSeasonDetails(seriesId)
    }

    private fun loadSeasonDetails(seriesId: Long) {
        getSeriesName(seriesId)
        getSeasonDetails(seriesId)
    }

    private fun getSeriesName(seriesId: Long) {
        tryToCall(
            block = { manageSeriesUseCase.getSeriesById(seriesId).title },
            onSuccess = { seriesTitle -> updateState { it.copy(seriesTitle = seriesTitle) } },
            onError = { updateState { it.copy(errorStatus = ErrorStatus.UNKNOWN_ERROR) } },
            dispatcher = dispatcher
        )
    }

    private fun getSeasonDetails(seriesId: Long) {
        tryToCall(
            onStart = { updateState { it.copy(seasonSectionState = ScreenStatus.LOADING) } },
            block = { manageSeriesUseCase.getSeriesSeasons(seriesId) },
            onSuccess = ::setSeasonDetailsToUiState,
            onError = { throwable -> setError(throwable) { copy(seasonSectionState = ScreenStatus.ERROR) } },
            dispatcher = dispatcher
        )
    }

    private fun setSeasonDetailsToUiState(seasons: List<Season>) {
        updateState { currentState ->
            currentState.copy(
                seasonSectionState = ScreenStatus.SUCCESS,
                season = seasons.map { it.toUiState() }
            )
        }
    }

    private fun setError(
        throwable: Throwable,
        updateSection: SeasonDetailsScreenState.() -> SeasonDetailsScreenState
    ) {
        updateState {
            it.updateSection().copy(
                errorStatus = handleDetailsException(throwable)
            )
        }
    }

    private fun handleDetailsException(e: Throwable): ErrorStatus {
        return when (e) {
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }

    override fun onBackClicked() {
        sendEffect(SeasonDetailEffect.NavigateBack)
    }

    override fun onSeasonClicked(seriesId: Long, seasonNumber: Int) {
        sendEffect(SeasonDetailEffect.NavigateToEpisodesScreen(seriesId, seasonNumber))
    }
}