package com.cairosquad.viewmodel.details.artist

import com.cairosquad.viewmodel.exception.ErrorStatus


sealed class ArtistEffect {
    data class NavigateToMovieDetails(val movieId : Long): ArtistEffect()
    object NavigateBack: ArtistEffect()
    data class ErrorHappened(val message: ErrorStatus) : ArtistEffect()
}