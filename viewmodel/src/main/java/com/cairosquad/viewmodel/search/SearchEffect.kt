package com.cairosquad.viewmodel.search

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class SearchEffect {
    data class ErrorHappened(val message: ErrorStatus) : SearchEffect()
    data class NavigateToMovieDetails(val movieId: Long) : SearchEffect()
    data class NavigateToSeriesDetails(val seriesId: Long) : SearchEffect()
    data class NavigateToArtistDetails(val artistId: Long) : SearchEffect()
    object NavigateToSeeAllForYouScreen : SearchEffect()
    object HideKeyboard : SearchEffect()
}