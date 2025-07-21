package com.cairosquad.viewmodel.foryou

import com.cairosquad.viewmodel.search.SearchEffect

sealed class ForYouEffect {
    data class NavigateToMovieDetails(val movieId: Long) : ForYouEffect()
}