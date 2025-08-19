package com.cairosquad.viewmodel.library.view_all_lists

interface ViewAllListsInteractionListener {

    fun onNavigateBack()
    fun onSeriesListClick(listId: Long, listName: String)
    fun onMovieListClick(listId: Long, listName: String)
    fun onCreateNewListClick()
    fun onRefresh()

    fun onAddListClick()
    fun onDismissCreateListBottomSheet()
    fun onListValueChange(listName: String)
    fun onSubmitCreateListClick()
}