package searchviewmodel

sealed class SearchUiState {
    object Idle : SearchUiState()          // Initial state
    object Loading : SearchUiState()       // Loading progress
    data class Success(val results: List<com.cairosquad.entity.Movie>) : SearchUiState()  // Results loaded
    data class Error(val message: String) : SearchUiState()  // Error occurred
    object Empty : SearchUiState()        // No results found
}