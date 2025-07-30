package com.cairosquad.viewmodel.details.series.season

interface SeasonDetailsInteractionListener {
    fun onBackClicked()
    fun onSeasonClicked(seriesId: Long, seasonNumber: Int)
}