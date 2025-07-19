package com.cairosquad.viewmodel.details.episodes

import android.util.Log
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Episode
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
        Log.d("asdasd", ": $seriesId | $seasonNumber")
        getSeasons(seriesId)
        getEpisodes(seriesId, seasonNumber)
    }

    private fun getEpisodes(seriesId: Long, seasonNumber: Int) {
        tryToCall(
            onStart = {
                updateState { it.copy(episodesSectionState = ScreenStatus.LOADING) }
            },
            block = { seriesDetailsUseCase.getEpisodes(seriesId, seasonNumber) },
            onSuccess = ::setEpisodesToUiState,
            onError = { throwable ->
                setError(throwable) { copy(episodesSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setEpisodesToUiState(episodes: List<Episode>) {
        updateState { currentState ->
            val selectedSeason = currentState.seasons.firstOrNull { it.seasonNumber == seasonNumber }

            currentState.copy(
                episodesSectionState = ScreenStatus.SUCCESS,
                episodes = episodes.map { it.toUiState() },
                season = EpisodesDetailsScreenState.SeasonUiState(
                    posterUrl = selectedSeason?.posterUrl.orEmpty(),
                    seasonNumber = seasonNumber,
                    episodesCount = selectedSeason?.episodesCount ?: 0
                ),
                selectedSeasonNumber = seasonNumber,
                currentSeasonNumber = seasonNumber
            )
        }
    }



    private fun getSeasons(seriesId: Long) {
        tryToCall(
            onStart = {},
            block = { seriesDetailsUseCase.getSeriesSeasons(seriesId) },
            onSuccess = { seasons ->
                updateState {
                    it.copy(
                        seasons = seasons.map { season ->
                            EpisodesDetailsScreenState.SeasonUiState(
                                seasonNumber = season.seasonNumber,
                                posterUrl = season.posterPath,
                                episodesCount = season.episodesCount
                            )
                        }
                    )
                }
            },
            onError = {
                // Optional: Handle error for season list
                Log.d("asdas", "getSeasons: $it")
            },
            dispatcher = Dispatchers.IO
        )
    }


    override fun onBackClick() {
        sendEffect(EpisodesDetailEffect.NavigateBack)
    }

    override fun onVideoClick(videoId: String) {
        sendEffect(EpisodesDetailEffect.PlayEpisode)
    }

    override fun onSeasonsDropdownClick() {
        updateState { it.copy(isSeasonDropdownExpanded = !it.isSeasonDropdownExpanded) }
    }

    override fun onSeasonSelected(seriesId: Long, seasonNumber: Int) {
        if (this.seasonNumber == seasonNumber) return
        this.seasonNumber = seasonNumber
        updateState {
            it.copy(
                episodesSectionState = ScreenStatus.LOADING,
                selectedSeasonNumber = seasonNumber,
                isSeasonDropdownExpanded = false
            )
        }
        getEpisodes(seriesId, seasonNumber)
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
