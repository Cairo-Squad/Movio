package com.cairosquad.viewmodel.home.effect

sealed class DiscoverEffect {
    data class NavigateMovie(val movieId :Long) : DiscoverEffect()
    data class NavigateSeries(val seriesId :Long) : DiscoverEffect()
}