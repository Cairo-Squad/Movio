package com.cairosquad.viewmodel.home

sealed class HomeEffect {
    data object NavigateToProfile: HomeEffect()
    data class NavigateMovie(val movieID :Long) : HomeEffect()
    data class NavigateSeries(val movieID :Long) : HomeEffect()
    data class NavigateToSeeAllTopRated(val isMovie :Boolean): HomeEffect()
    data object NavigateToSeeAllTrending: HomeEffect()
    data object NavigateToSeeAllFreeToWatch: HomeEffect()
    data object NavigateToSeeAllUpcoming: HomeEffect()
    data class NavigateToSeeAllMoreRecommended(val isMovie :Boolean): HomeEffect()
    data object NavigateToSeeAllAiringToday: HomeEffect()
    data object NavigateToSeeAllOnTv: HomeEffect()
}
