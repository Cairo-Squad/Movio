package com.cairosquad.viewmodel.details.series.season

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SeasonsViewModel.Factory::class)
class SeasonsViewModel @AssistedInject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase,
    @Assisted private val seriesId: Long,
    @Assisted private val dispatcher: CoroutineDispatcher
) : BaseViewModel<SeasonDetailsScreenState, SeasonDetailEffect>(SeasonDetailsScreenState()),
    SeasonDetailsInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(
            seriesId: Long,
            dispatcher: CoroutineDispatcher
        ): SeasonsViewModel
    }

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

    override fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            loadSeasonDetails(seriesId)
        }
    }
}