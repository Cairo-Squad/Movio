package com.cairosquad.viewmodel.library.view_all_lists

import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.library.LibraryScreenState.ListsUiState

data class ViewAllListsScreenState(
    val screenStatus: SectionStatus = SectionStatus.LOADING,
    val movieLists: List<ListsUiState> = emptyList(),
    val seriesLists: List<ListsUiState> = emptyList(),

    val isCreateListBottomSheetVisible: Boolean = false,

    val errorStatus: ErrorStatus? = null,
    val isRefreshing: Boolean = false,
) {
    enum class SectionStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}