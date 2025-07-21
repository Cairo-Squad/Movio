package com.cairosquad.viewmodel.foryou

import androidx.paging.PagingData
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState.ScreenStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ForYouState(
    val forYou: Flow<PagingData<MovieUiState>> = flowOf(PagingData.empty()),
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val isRefreshing: Boolean = false
){
    data class MovieUiState(
        val id: Long,
        val title: String,
        val rating: Float,
        val posterPath: String,
    )

    enum class ScreenStatus {
        LOADING,
        FAILED
    }

}
