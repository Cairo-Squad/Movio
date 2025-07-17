package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.viewmodel.exception.ErrorStatus

data class EpisodesDetailsScreenState(
    val basicDetailsSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val episodesSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val errorStatus: ErrorStatus? = null,
    val season: SeasonUiState = SeasonUiState(),
    val episodes: List<EpisodeUiState> = emptyList()
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
        INITIAL,
        LOADING,
        SUCCESS,
        ERROR
    }
}


