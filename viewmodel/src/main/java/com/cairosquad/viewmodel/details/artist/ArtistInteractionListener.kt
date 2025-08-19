package com.cairosquad.viewmodel.details.artist

interface ArtistInteractionListener {
    fun onBackClick()
    fun onMovieClick(movieId : Long)
    fun onSeriesClick(seriesId: Long)
    fun onRefresh()
}