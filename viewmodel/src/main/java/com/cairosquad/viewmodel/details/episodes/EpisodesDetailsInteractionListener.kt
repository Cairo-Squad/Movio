package com.cairosquad.viewmodel.details.episodes

interface EpisodesDetailsInteractionListener {
    fun onBackClick()
    fun onVideoClick(videoId: String)
    fun onSeasonsDropdownClick()
    fun onSeasonSelected(seriesId: Long, seasonNumber: Int)
    val isSeasonDropdownExpanded: Boolean
        get() = false
}