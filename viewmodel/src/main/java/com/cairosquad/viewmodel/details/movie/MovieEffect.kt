package com.cairosquad.viewmodel.details.movie

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed interface MovieEffect {

    data object NavigateBack : MovieEffect
    data object PlayTrailer : MovieEffect

    data class NavigateToActor(val actorId: Long) : MovieEffect
    data class NavigateToMovie(val movieId: Long) : MovieEffect

    data class NavigateToSimilarMovies(val movieId: Long) : MovieEffect
    data class NavigateToAllActors(val movieId: Long) : MovieEffect
    data class NavigateToAllReviews(val movieId: Long) : MovieEffect

    data class ErrorHappened(val message: ErrorStatus) : MovieEffect
    data object NavigateToLogin : MovieEffect
}