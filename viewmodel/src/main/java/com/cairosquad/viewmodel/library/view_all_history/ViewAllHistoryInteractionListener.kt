package com.cairosquad.viewmodel.library.view_all_history

interface ViewAllHistoryInteractionListener {
    fun onBackClicked()

    fun onMovieClicked(movieId: Long)

    fun onSeriesClicked(seriesId: Long)

    fun onRefresh()
}