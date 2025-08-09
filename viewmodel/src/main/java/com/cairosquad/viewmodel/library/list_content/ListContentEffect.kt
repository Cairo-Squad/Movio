package com.cairosquad.viewmodel.library.list_content

sealed class ListContentEffect {
    data object OnNavigateBack: ListContentEffect()
    data class OnMovieClicked(val movieId: Long): ListContentEffect()
    data class OnSeriesClicked(val seriesId: Long): ListContentEffect()
}