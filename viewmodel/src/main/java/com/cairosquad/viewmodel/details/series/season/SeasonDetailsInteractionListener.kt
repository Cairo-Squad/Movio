package com.cairosquad.viewmodel.details.series.season

interface SeasonDetailsInteractionListener {
    fun onBackClicked()
    fun onEpisodeClicked(episodeId: Long, seasonNumber: Int)
}