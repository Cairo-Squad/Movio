package com.cairosquad.viewmodel.library.view_all_favorite


sealed class ViewAllFavoriteEffect {
    data object OnNavigateBack: ViewAllFavoriteEffect()
    data class OnMovieClicked(val movieId: Long): ViewAllFavoriteEffect()
    data class OnSeriesClicked(val seriesId: Long): ViewAllFavoriteEffect()
}