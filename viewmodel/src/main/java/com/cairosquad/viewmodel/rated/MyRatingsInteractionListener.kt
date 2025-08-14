package com.cairosquad.viewmodel.rated

interface MyRatingsInteractionListener {
    fun onBackPressed()
    fun onUndoClicked()
    fun onMovieClicked(movieId: Long)
    fun onSeriesClicked(seriesId: Long)
    fun onMovieDelete(movieId: Long, rating: Int)
    fun onSeriesDelete(seriesId: Long, rating: Int)
}