package com.cairosquad.viewmodel.home

sealed class HomeEffect {
    data object NavigateToProfile: HomeEffect()
    data class NavigateToCategory(val category:String): HomeEffect()
    data class NavigateToFeaturedMovie(val movieID :Long) : HomeEffect()
    data class NavigateMovie(val movieID :Long) : HomeEffect()
    data class NavigateSeries(val movieID :Long) : HomeEffect()
    data object NavigateToSeeAllTopRated: HomeEffect()
    data object NavigateToSeeAllTrending: HomeEffect()
    data object NavigateToSeeAllFreeToWatch: HomeEffect()
    data object NavigateToSeeAllUpcoming: HomeEffect()
    data object NavigateToSeeAllMoreRecommended: HomeEffect()
    data object NavigateToSeeAllAiringToday: HomeEffect()
    data object NavigateToSeeAllOnTv: HomeEffect()
}
