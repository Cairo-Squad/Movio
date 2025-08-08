package com.cairosquad.viewmodel.library.view_all_lists

interface ViewAllListsInteractionListener {

    fun onNavigateBack()
    fun onSeriesListClicked(listId: Long, listName: String)
    fun onMovieListClicked(listId: Long, listName: String)
    fun onCreateNewListClicked()
    fun onRefresh()

    fun onAddListClicked()
    fun onDismissCreateListBottomSheet()
    fun onListValueChange(listName: String)
    fun onSubmitCreateListClicked()
}