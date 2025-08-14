package com.cairosquad.viewmodel.search

sealed class SearchEffect {
    data class NavigateToMovieDetails(val movieId: Long) : SearchEffect()
    data class NavigateToSeriesDetails(val seriesId: Long) : SearchEffect()
    data class NavigateToArtistDetails(val artistId: Long) : SearchEffect()
    object NavigateToSeeAllForYouScreen : SearchEffect()
    object HideKeyboard : SearchEffect()
}