package com.cairosquad.viewmodel.library

sealed class LibraryEffect {

    data class NavigateToMovieDetails(val movieId: Long) : LibraryEffect()
    data class NavigateToSeriesDetails(val seriesId: Long) : LibraryEffect()
    data class NavigateToListDetails(val listId: Long) : LibraryEffect()

    object NavigateToFavorites : LibraryEffect()
    object NavigateToHistory : LibraryEffect()
    object NavigateToLists : LibraryEffect()
}