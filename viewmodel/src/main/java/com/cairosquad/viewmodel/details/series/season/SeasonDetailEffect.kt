package com.cairosquad.viewmodel.details.series.season

sealed class SeasonDetailEffect {
    data object NavigateBack : SeasonDetailEffect()
    data class NavigateToEpisodesScreen(val seriesId: Long, val seasonNumber: Int) : SeasonDetailEffect()
}