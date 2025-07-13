package com.cairosquad.viewmodel.searchviewmodel

data class SearchScreenState(
    val query: String = "",
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorMessage: String? = null,
    val recentSearch: List<String> = emptyList(),
    val forYou: List<MovieScreenState> = emptyList(),
    val exploreMore: List<MovieScreenState> = emptyList(),
    val movies: List<MovieScreenState> = emptyList(),
    val series: List<SeriesScreenState> = emptyList(),
    val artists: List<ArtistScreenState> = emptyList(),
    val isRefreshing: Boolean = false
) {
    data class ArtistScreenState(
        val id: Long,
        val name: String,
        val photoPath: String
    )

    data class MovieScreenState(
        val id: Long,
        val title: String,
        val rating: Float,
        val posterPath: String,
    )

    data class SeriesScreenState(
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