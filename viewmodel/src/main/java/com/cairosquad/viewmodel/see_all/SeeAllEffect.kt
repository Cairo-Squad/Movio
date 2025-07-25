package com.cairosquad.viewmodel.see_all

sealed class SeeAllEffect {
    data class NavigateMediaDetails(val mediaId :Long, val isMovie: Boolean) : SeeAllEffect()
    data object NavigateBack: SeeAllEffect()
}