package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.viewmodel.exception.ErrorStatus

data class EpisodesDetailsScreenState(
    val basicDetailsSectionState: ScreenStatus = ScreenStatus.LOADING,
    val episodesSectionState: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val season: SeasonUiState = SeasonUiState(),
    val episodes: List<EpisodeUiState> = emptyList(),
    val seasons: List<SeasonUiState> = emptyList(),
    val selectedSeasonNumber: Int = 1,
    val isSeasonDropdownExpanded: Boolean = false,
    val isRefreshing: Boolean = false,
    val currentSeasonNumber: Int = 1
    ) {
    data class EpisodeUiState(
        val id: Long = 0L,
        val name: String = "",
        val number: Int = 0,
        val runtime: Int = 0,
        val rating: Float = 0F,
        val imageUrl: String = ""
    )

    data class SeasonUiState(
        val seasonNumber: Int = 0,
        val posterUrl: String = "",
        val episodesCount: Int = 0
    )

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}


