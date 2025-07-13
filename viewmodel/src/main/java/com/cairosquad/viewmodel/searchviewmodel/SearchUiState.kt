package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.viewmodel.base.BaseUiState

data class SearchUiState(
    val query: String = "",
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorMessage: String? = null,
    val recentSearch: List<String> = emptyList(),
    val forYou: List<MovieUiState> = emptyList(),
    val exploreMore: List<MovieUiState> = emptyList(),
    val movies: List<MovieUiState> = emptyList(),
    val series: List<SeriesUiState> = emptyList(),
    val artists: List<ArtistUiState> = emptyList(),
    override val isRefreshing: Boolean = false
) : BaseUiState {
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