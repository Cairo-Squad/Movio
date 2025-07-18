package com.cairosquad.viewmodel.details.series.season

import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SeasonsViewModel(
    private val seriesDetailsUseCase: GetSeriesDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    seriesId: Long,
    seasonNumber: Int
) : BaseViewModel<SeasonDetailsScreenState, SeasonDetailEffect>(SeasonDetailsScreenState()),
    SeasonDetailsInteractionListener {
        init {
            loadSeasonDetails(seriesId, seasonNumber)
        }

    private fun loadSeasonDetails(seriesId: Long, seasonNumber: Int) {
        getSeriesName(seriesId)
        getSeasonDetails(seriesId, seasonNumber)
        getEpisodes(seriesId, seasonNumber)
    }
    private fun getSeriesName(seriesId: Long) {
        tryToCall(
            block = { seriesDetailsUseCase.getSeries(seriesId).title },
            onSuccess = { seriesTitle -> updateState { it.copy(seriesTitle = seriesTitle) } },
            onError = { },
            dispatcher = dispatcher
        )
    }
    private fun getSeasonDetails(seriesId: Long, seasonNumber: Int) {
        tryToCall(
            onStart = { updateState { it.copy(seasonSectionState = ScreenStatus.LOADING) } },
            block = { seriesDetailsUseCase.getSeriesSeasons(seriesId) },
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

    private fun getEpisodes(seriesId: Long, seasonNumber: Int) {
        tryToCall(
            onStart = { updateState { it.copy(episodesSectionState = ScreenStatus.LOADING) } },
            block = { seriesDetailsUseCase.getEpisodes(seriesId, seasonNumber) },
            onSuccess = ::setEpisodesToUiState,
            onError = { throwable -> setError(throwable) { copy(episodesSectionState = ScreenStatus.ERROR) } },
            dispatcher = dispatcher
        )
    }

    private fun setEpisodesToUiState(episodes: List<Episode>) {
        updateState {
            it.copy(
                episodesSectionState = ScreenStatus.SUCCESS,
                episodes = episodes.map { it.toUiState() }
            )
        }
    }

    private fun setError(throwable: Throwable, updateSection: SeasonDetailsScreenState.() -> SeasonDetailsScreenState) {
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

    override fun onEpisodeClicked(episodeId: Long, seasonNumber: Int) {
        sendEffect(SeasonDetailEffect.NavigateToEpisodeDetails(episodeId, seasonNumber))
    }
}