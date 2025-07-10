package com.cairosquad.viewmodel.searchviewmodel

data class SearchUiState(
    val isIdle: Boolean = true,
    val isSearchFocused: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val searchSuggestions: List<String>? = null,

    val forYou: List<MovieUiState>? = null,
    val exploreMore: List<MovieUiState>? = null,

    val topResult: MovieUiState? = null,
    val movies: List<MovieUiState> = emptyList(),
    val series: List<SeriesUiState> = emptyList(),
    val artists: List<ArtistUiState> = emptyList(),
)