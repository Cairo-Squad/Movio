package com.cairosquad.viewmodel.details.artist

interface ArtistInteractionListener {
    fun onClickBack()
    fun onMovieClick(movieId : Long)
    fun onSeriesClick(seriesId: Long)
}