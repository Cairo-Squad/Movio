package com.cairosquad.viewmodel.details.series.season

interface SeasonDetailsInteractionListener {
    fun onBackClicked()
    fun onSeasonClicked(seasonId: Long, seasonNumber: Int)
}