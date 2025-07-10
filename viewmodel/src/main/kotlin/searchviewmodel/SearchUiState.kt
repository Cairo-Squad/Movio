package searchviewmodel

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Series

data class SearchUiState(
    val isIdle: Boolean = true,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val errorMessage: String? = null,

    val searchSuggestions: List<String>? = null,

    val forYou: List<MovieUiState>? = null,
    val exploreMore: List<MovieUiState>? = null,

    val topResult: MovieUiState? = null,
    val movies: List<MovieUiState>? = null,
    val series: List<SeriesUiState> = emptyList(),
    val artists: List<ArtistUiState> = emptyList(),
)


