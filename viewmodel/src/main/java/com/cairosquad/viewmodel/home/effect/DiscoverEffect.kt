package com.cairosquad.viewmodel.home.effect

import com.cairosquad.viewmodel.home.model.DiscoverType
import com.cairosquad.viewmodel.home.model.MediaType

sealed class DiscoverEffect {
    data class NavigateMovie(val movieId :Long) : DiscoverEffect()
    data class NavigateSeries(val seriesId :Long) : DiscoverEffect()
    data class NavigateToDiscover(val type: DiscoverType, val mediaType: MediaType): DiscoverEffect()
    data class NavigateToSeeAllFreeToWatch(val type: DiscoverType, val mediaType: MediaType): DiscoverEffect()
    data class NavigateToSeeAllUpcoming(val type: DiscoverType, val mediaType: MediaType): DiscoverEffect()
    data class NavigateToSeeAllTopRating(val type: DiscoverType, val mediaType: MediaType): DiscoverEffect()
    data class NavigateToSeeAllTrending(val type: DiscoverType, val mediaType: MediaType): DiscoverEffect()
}