package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.domain.search.exception.MovioException
import com.cairosquad.domain.search.usecase.GetSeriesDetailsUseCase
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import kotlinx.coroutines.Dispatchers

class EpisodesDetailsViewModel(
    private val seriesDetailsUseCase: GetSeriesDetailsUseCase,
    private val seriesId: Long,
    private var seasonNumber: Int
) : BaseViewModel<EpisodesDetailsScreenState, EpisodesDetailEffect>(EpisodesDetailsScreenState()),
    EpisodesDetailsInteractionListener {

    init {
      loadBasicDetails()
        getEpisodes(seriesId,seasonNumber)
    }

    private fun loadBasicDetails() {
        tryToCall(
            onStart = {
                updateState { it.copy(basicDetailsSectionState = ScreenStatus.LOADING) }
            },
            block = {
                seriesDetailsUseCase.getSeasonDetails(seriesId, seasonNumber)
            },
            onSuccess = ::setBasicSeriesDetailsToUiState,
            onError = { throwable ->
                setError(throwable) { copy(basicDetailsSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }
    private fun setBasicSeriesDetailsToUiState(season: Season) {
        updateState {
            it.copy(
                basicDetailsSectionState = ScreenStatus.SUCCESS,
                season = season.toUiState()
            )
        }
    }

    private fun getEpisodes(seriesId: Long,seasonNumber: Int) {
        tryToCall(
            onStart = {
                updateState { it.copy(episodesSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getEpisodes(seriesId, seasonNumber)},
            onSuccess = ::setEpisodesToUiState,
            onError = { throwable ->
                setError(throwable) { copy(episodesSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
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

    override fun onBackClick() {
        sendEffect(EpisodesDetailEffect.NavigateBack)
    }

    override fun onVideoClick(videoId: String) {
        sendEffect(EpisodesDetailEffect.PlayEpisodes)
    }

    override fun onSeasonsDropdownClick() {
        //updateState { it.copy(isSeasonDropdownExpanded = !it.isSeasonDropdownExpanded) }
    }

    override fun onSeasonSelected(seriesId: Long, seasonNumber: Int) {
        this.seasonNumber = seasonNumber
        loadBasicDetails()
        getEpisodes(seriesId,seasonNumber)
    }

    private fun setError(
        throwable: Throwable,
        updateSection: EpisodesDetailsScreenState.() -> EpisodesDetailsScreenState
    ) {
        updateState {
            it.updateSection().copy(
                errorStatus = handleDetailsException(throwable)
            )
        }
    }

    private fun handleDetailsException(error: Throwable): ErrorStatus {
        return when (error) {
            is MovioException -> exceptionToErrorStatus(error)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}
