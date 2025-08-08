package com.cairosquad.viewmodel.rated

sealed interface MyRatingsEffect {
    data object NavigateBack : MyRatingsEffect
    data class NavigateToMovieDetails(val movieId: Long) : MyRatingsEffect
    data class NavigateToSeriesDetails(val seriesId: Long) : MyRatingsEffect
}