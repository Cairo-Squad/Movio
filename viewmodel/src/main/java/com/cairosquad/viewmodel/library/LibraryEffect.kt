package com.cairosquad.viewmodel.library

sealed class LibraryEffect {

    data class NavigateToMovieDetails(val movieId: Long) : LibraryEffect()
    data class NavigateToSeriesDetails(val seriesId: Long) : LibraryEffect()
    data class NavigateToListDetails(val listId: Long, val listName: String) : LibraryEffect()

    data object NavigateToFavorites : LibraryEffect()
    data object NavigateToHistory : LibraryEffect()
    data object NavigateToLists : LibraryEffect()
    data object NavigateToLogin: LibraryEffect()
}