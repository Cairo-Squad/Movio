package com.cairosquad.viewmodel.rated

interface MyRatingsInteractionListener {
    fun onBackPressed()
    fun onUndoClick()
    fun onMovieClick(movieId: Long)
    fun onSeriesClick(seriesId: Long)
    fun onMovieDelete(movieId: Long, rating: Double)
    fun onSeriesDelete(seriesId: Long, rating: Double)
}