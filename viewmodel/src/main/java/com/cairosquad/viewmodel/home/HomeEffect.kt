package com.cairosquad.viewmodel.home

import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType


sealed class HomeEffect {
    data object NavigateToProfile: HomeEffect()
    data class NavigateMediaDetails(val mediaId :Long, val isMovie: Boolean) : HomeEffect()
    data class NavigateToSeeAllScreen(
        val mediaContentType: MediaContentType,
        val mediaType: MediaType
    ): HomeEffect()
}