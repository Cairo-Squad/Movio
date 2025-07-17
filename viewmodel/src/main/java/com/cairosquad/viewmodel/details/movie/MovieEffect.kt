package com.cairosquad.viewmodel.details.movie

sealed interface MovieEffect {
    data class NavigateToActor(val actorId: Long) : MovieEffect
    data class NavigateToMovie(val movieId: Long) : MovieEffect
    // TODO: Continue
}