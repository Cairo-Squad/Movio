package com.cairosquad.viewmodel.library

interface LibraryInteractionListener {

    fun onListsViewAllClick()
    fun onFavoritesViewAllClick()
    fun onHistoryViewAllClick()
    fun onLoginClicked()
    fun onRefresh()

    fun onListClicked(listId: Long, listName: String)
    fun onMovieClicked(movieId: Long)
    fun onSeriesClicked(seriesId: Long)

}