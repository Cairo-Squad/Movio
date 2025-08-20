package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Episode
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

@HiltViewModel(assistedFactory = EpisodesDetailsViewModel.Factory::class)
class EpisodesDetailsViewModel @AssistedInject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase,
    @Assisted private val seriesId: Long,
    @Assisted private var seasonNumber: Int = 0
) : BaseViewModel<EpisodesDetailsScreenState, EpisodesDetailEffect>(EpisodesDetailsScreenState()),
    EpisodesDetailsInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(seriesId: Long, seasonNumber: Int): EpisodesDetailsViewModel
    }

    init {
        initScreen()
    }

    private fun initScreen() {
        getSeasons(seriesId)
        getEpisodes(seriesId, seasonNumber)
    }

    private fun getEpisodes(seriesId: Long, seasonNumber: Int) {
        tryToCall(
            onStart = ::setEpisodesLoading,
            block = { manageSeriesUseCase.getEpisodes(seriesId, seasonNumber) },
            onSuccess = ::handleEpisodesSuccess,
            onError = { throwable ->
                setError(throwable) { copy(episodesSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun getSeasons(seriesId: Long) {
        tryToCall(
            onStart = ::setSeasonsLoading,
            block = { manageSeriesUseCase.getSeriesSeasons(seriesId) },
            onSuccess = ::handleSeasonsSuccess,
            onError = ::handleSeasonsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun setEpisodesLoading() {
        updateState { it.copy(episodesSectionState = ScreenStatus.LOADING) }
    }

    private fun handleEpisodesSuccess(episodes: List<Episode>) {
        updateState { currentState ->
            val selectedSeason =
                currentState.seasons.firstOrNull { it.seasonNumber == seasonNumber }

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

    private fun setSeasonsLoading() {
        updateState { it.copy(basicDetailsSectionState = ScreenStatus.LOADING) }
    }

    private fun handleSeasonsSuccess(seasons: List<com.cairosquad.entity.Season>) {
        updateState {
            it.copy(
                basicDetailsSectionState = ScreenStatus.SUCCESS,
                seasons = seasons.map { season ->
                    EpisodesDetailsScreenState.SeasonUiState(
                        seasonNumber = season.seasonNumber,
                        posterUrl = season.posterPath,
                        episodesCount = season.episodesCount
                    )
                }
            )
        }
    }

    private fun handleSeasonsError(error: Throwable) {
        setError(error) { copy(basicDetailsSectionState = ScreenStatus.ERROR) }
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

    override fun onRefresh() {
        getSeasons(seriesId)
        getEpisodes(seriesId, seasonNumber)
    }

    private fun setError(
        throwable: Throwable,
        updateSection: EpisodesDetailsScreenState.() -> EpisodesDetailsScreenState
    ) {
        updateState { it.updateSection().copy(errorStatus = handleDetailsException(throwable))}
    }

    private fun handleDetailsException(error: Throwable): ErrorStatus {
        return if (error is MovioException) exceptionToErrorStatus(error)
        else ErrorStatus.UNKNOWN_ERROR
    }
}