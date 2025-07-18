package com.cairosquad.viewmodel.details.series.season

sealed class SeasonDetailEffect {
    data object NavigateBack : SeasonDetailEffect()
    data class NavigateToEpisodeDetails(val episodeId: Long, val seasonNumber: Int) : SeasonDetailEffect()
}