package com.cairosquad.viewmodel.details.artist

import com.cairosquad.viewmodel.exception.ErrorStatus


sealed class ArtistEffect {
    data class NavigateToMovieDetails(val movieId : Long): ArtistEffect()
    data class NavigateToSeriesDetails(val seriesId : Long): ArtistEffect()

    object NavigateBack: ArtistEffect()
    data class ErrorHappened(val message: ErrorStatus) : ArtistEffect()
}