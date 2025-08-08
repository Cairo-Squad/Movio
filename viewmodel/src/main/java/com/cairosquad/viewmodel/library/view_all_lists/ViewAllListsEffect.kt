package com.cairosquad.viewmodel.library.view_all_lists

sealed class ViewAllListsEffect {

    data object OnNavigateBack: ViewAllListsEffect()
    data class OnMovieListClicked(val listId: Long, val listName: String): ViewAllListsEffect()
    data class OnSeriesListClicked(val listId: Long, val listName: String): ViewAllListsEffect()
}