package com.cairosquad.viewmodel.foryou

sealed class ForYouEffect {
    data class NavigateToMovieDetails(val movieId: Long) : ForYouEffect()
}