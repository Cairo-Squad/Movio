package com.cairosquad.viewmodel.library.view_all_favorite

interface ViewAllFavoriteInteractionListener {

    fun onBackClicked()

    fun onMovieClicked(movieId: Long)

    fun onSeriesClicked(seriesId: Long)

    fun onMovieDelete(movieId: Long)

    fun onSeriesDelete(seriesId: Long)

    fun onRefresh()
}