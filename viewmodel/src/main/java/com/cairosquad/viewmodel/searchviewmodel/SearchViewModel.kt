package com.cairosquad.viewmodel.searchviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.GetRecentSearchUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val getExploreMoreUseCase: GetExploreMoreUseCase,
    private val getForYouUseCase: GetForYouUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), SearchInteractionListener {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun loadDiscoverMovies() = viewModelScope.launch(dispatcher) {
        try {
            val forYou = getForYouUseCase.getForYouMovies().map { it.toUiState() }
            val exploreMore = getExploreMoreUseCase.getExploreMoreMovies().map { it.toUiState() }

            _uiState.update {
                it.copy(
                    isIdle = false,
                    isLoading = false,
                    errorMessage = null,
                    forYou = forYou,
                    exploreMore = exploreMore
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Failed to load discover content."
                )
            }
        }
    }

    override fun onQueryTextChanged(query: String) {
        viewModelScope.launch(dispatcher) {
            val suggestions = getRecentSearchUseCase.getByQuery(query)
            _uiState.update {
                it.copy(
                    isIdle = false,
                    isLoading = false,
                    errorMessage = null,
                    searchSuggestions = suggestions
                )
            }
        }
    }


    override fun onSearch(query: String) {
        if (query.isBlank()) {
            _uiState.update { SearchUiState(isIdle = true) }
            return
        }

        searchJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        searchJob = viewModelScope.launch(dispatcher) {
            try {
                val movies = searchUseCase.searchMovies(query).map { it.toUiState() }
                val series = searchUseCase.searchSeries(query).map { it.toUiState() }
                val artists = searchUseCase.searchArtists(query).map { it.toUiState() }

                val topResult = movies.firstOrNull()

                if (movies.isEmpty() && series.isEmpty() && artists.isEmpty()) {
                    _uiState.update { it.copy(isLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            topResult = topResult,
                            movies = movies,
                            series = series,
                            artists = artists
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = ex.localizedMessage ?: "Unknown error"
                    )
                }
            }
        }
    }

    override fun onHistoryItemClicked(query: String) = onSearch(query)

    override fun onClearHistory() {
        viewModelScope.launch(dispatcher) {
            clearRecentSearchUseCase.clearAll()
            _uiState.update { it.copy(searchSuggestions = emptyList()) }
        }
    }

    override fun onRemoveHistoryItem(query: String) {
        viewModelScope.launch(dispatcher) {
            clearRecentSearchUseCase.removeQuery(query)
            val suggestions = getRecentSearchUseCase.getAll()
            _uiState.update { it.copy(searchSuggestions = suggestions) }
        }
    }

}
