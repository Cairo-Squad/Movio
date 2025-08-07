package com.cairosquad.viewmodel.library.list_content

interface ListContentInteractionListener {

    fun onBackClicked()

    fun onMovieClicked(movieId: Long)

    fun onSeriesClicked(seriesId: Long)

    fun onMovieDelete(movieId: Long)

    fun onSeriesDelete(seriesId: Long)

    fun onRefresh()
}