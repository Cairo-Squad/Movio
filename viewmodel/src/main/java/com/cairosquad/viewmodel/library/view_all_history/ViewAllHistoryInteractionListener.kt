package com.cairosquad.viewmodel.library.view_all_history

interface ViewAllHistoryInteractionListener {
    fun onBackClicked()

    fun onMovieClicked(movieId: Long)

    fun onSeriesClicked(seriesId: Long)

    fun onMovieDelete(movieId: Long)

    fun onSeriesDelete(seriesId: Long)

    fun onRefresh()

    fun onUndoClicked()
}