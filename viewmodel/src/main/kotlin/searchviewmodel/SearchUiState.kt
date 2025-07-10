package searchviewmodel

import com.cairosquad.entity.Movie

sealed class SearchUiState {
    object Idle : SearchUiState()          // Initial state
    object Loading : SearchUiState()       // Loading progress
    data class Success(val results: List<Movie>) : SearchUiState()  // Results loaded
    data class Error(val message: String) : SearchUiState()  // Error occurred
    object Empty : SearchUiState()        // No results found
    data class Discover(val forYou: List<Movie>, val exploreMore: List<Movie>) : SearchUiState()
}

data class UiState(
    val isLoading: Boolean = false,
    val isIdle: Boolean=false,
    val data: List<String>? = null,
    val errorMessage: String? = null,
    val Empty : Boolean =false,
)