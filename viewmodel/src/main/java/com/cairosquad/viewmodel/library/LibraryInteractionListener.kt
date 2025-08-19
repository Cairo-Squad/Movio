package com.cairosquad.viewmodel.library

interface LibraryInteractionListener {

    fun onListsViewAllClick()
    fun onFavoritesViewAllClick()
    fun onHistoryViewAllClick()
    fun onLoginClick()
    fun onRefresh()

    fun onListClick(listId: Long, listName: String)
    fun onMovieClick(movieId: Long)
    fun onSeriesClick(seriesId: Long)
}