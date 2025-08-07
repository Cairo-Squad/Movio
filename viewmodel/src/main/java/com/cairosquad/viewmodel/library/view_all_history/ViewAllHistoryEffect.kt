package com.cairosquad.viewmodel.library.view_all_history

sealed class ViewAllHistoryEffect {
    data object OnNavigateBack: ViewAllHistoryEffect()

    data class OnMovieClicked(val movieId: Long): ViewAllHistoryEffect()
    data class OnSeriesClicked(val seriesId: Long): ViewAllHistoryEffect()

}