package com.cairosquad.viewmodel.library.view_all_lists

import androidx.paging.PagingData
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.library.LibraryScreenState.ListsUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ViewAllListsScreenState(
    val screenStatus: SectionStatus = SectionStatus.LOADING,
    val movieLists: Flow<PagingData<ListsUiState>> = flowOf(PagingData.empty()),
    val seriesLists: Flow<PagingData<ListsUiState>> = flowOf(PagingData.empty()),

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