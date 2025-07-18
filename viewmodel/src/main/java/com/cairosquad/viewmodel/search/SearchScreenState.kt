package com.cairosquad.viewmodel.search

import androidx.paging.PagingData
import com.cairosquad.viewmodel.exception.ErrorStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchScreenState(
    val query: String = "",
    val selectedTabIndex: Int = 0,
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val recentSearch: List<String> = emptyList(),
    val forYou: List<MovieUiState> = emptyList(),
    val exploreMore: List<MovieUiState> = emptyList(),
    val movies: Flow<PagingData<MovieUiState>> = flowOf(PagingData.empty()),
    val series: Flow<PagingData<SeriesUiState>> = flowOf(PagingData.empty()),
    val artists: Flow<PagingData<ArtistUiState>> = flowOf(PagingData.empty()),
    val isRefreshing: Boolean = false
) {
    data class ArtistUiState(
        val id: Long,
        val name: String,
        val photoPath: String
    )

    data class MovieUiState(
        val id: Long,
        val title: String,
        val rating: Float,
        val posterPath: String,
    )

    data class SeriesUiState(
        val id: Long,
        val title: String,
        val rating: Float,
        val posterPath: String
    )

    enum class ScreenStatus {
        EXPLORE,
        SEARCH,
        RESULT,
        LOADING,
        FAILED
    }
}