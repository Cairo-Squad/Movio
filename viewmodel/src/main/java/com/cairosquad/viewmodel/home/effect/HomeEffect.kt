package com.cairosquad.viewmodel.home.effect

import com.cairosquad.viewmodel.home.model.DiscoverType
import com.cairosquad.viewmodel.home.model.MediaType

sealed class HomeEffect {
    data object NavigateToProfile: HomeEffect()
    data class NavigateMovie(val movieId :Long) : HomeEffect()
    data class NavigateSeries(val seriesId :Long) : HomeEffect()
    data object NavigateToSeeAllAiringToday: HomeEffect()
    data object NavigateToSeeAllOnTv: HomeEffect()
    data class NavigateToDiscover(val type: DiscoverType, val mediaType: MediaType): HomeEffect()
    data class NavigateToSeeAllFreeToWatch(val type: DiscoverType, val mediaType: MediaType): HomeEffect()
    data class NavigateToSeeAllUpcoming(val type: DiscoverType, val mediaType: MediaType): HomeEffect()
    data class NavigateToSeeAllTopRating(val type: DiscoverType, val mediaType: MediaType): HomeEffect()
    data class NavigateToSeeAllTrending(val type: DiscoverType, val mediaType: MediaType): HomeEffect()
}