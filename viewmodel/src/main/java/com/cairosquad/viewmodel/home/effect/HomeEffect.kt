package com.cairosquad.viewmodel.home.effect

sealed class HomeEffect {
    data object NavigateToProfile: HomeEffect()
    data class NavigateMovie(val movieId :Long) : HomeEffect()
    data class NavigateSeries(val seriesId :Long) : HomeEffect()
    data object NavigateToSeeAllAiringToday: HomeEffect()
    data object NavigateToSeeAllOnTv: HomeEffect()
}