package com.cairosquad.viewmodel.searchviewmodel

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
) {
    data class ArtistUiState(
        val id: Long,
        val name: String,
        val photoPath: String
    )

    data class MovieUiState(
        val id: Long = 0,
        val title: String = "Movie",
        val rating: Float = 0.0f,
        val posterPath: String = " ",
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