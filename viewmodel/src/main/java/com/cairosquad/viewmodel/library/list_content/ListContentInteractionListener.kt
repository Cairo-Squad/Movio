package com.cairosquad.viewmodel.library.list_content

interface ListContentInteractionListener {

    fun onBackClick()

    fun onMovieClick(movieId: Long)

    fun onSeriesClick(seriesId: Long)

    fun onMovieDelete(movieId: Long)

    fun onSeriesDelete(seriesId: Long)

    fun onRefresh()

    fun onUndoClick()
}