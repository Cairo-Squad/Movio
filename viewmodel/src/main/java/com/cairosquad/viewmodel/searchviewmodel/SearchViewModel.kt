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

    init {
        loadDiscoverMovies()
    }

    fun loadDiscoverMovies() = viewModelScope.launch(dispatcher) {
        try {
            val forYou = getForYouUseCase.getForYouMovies().map { it.toUiState() }
            val exploreMore = getExploreMoreUseCase.getExploreMoreMovies().map { it.toUiState() }

            _uiState.update {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                    forYou = forYou,
                    exploreMore = exploreMore
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.FAILED,
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
                    screenStatus = SearchUiState.ScreenStatus.SEARCH,
                    recentSearch = suggestions
                )
            }
        }
    }

    override fun onCancelSearch() {
        _uiState.update {
            it.copy(
                screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                query = "",
            )
        }
    }


    override fun onSearch(query: String) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                screenStatus = SearchUiState.ScreenStatus.LOADING,
                query = query,
            )
        }

        searchJob = viewModelScope.launch(dispatcher) {
            try {
                val movies = searchUseCase.searchMovies(query).map { it.toUiState() }
                val series = searchUseCase.searchSeries(query).map { it.toUiState() }
                val artists = searchUseCase.searchArtists(query).map { it.toUiState() }

                _uiState.update {
                    it.copy(
                        screenStatus = SearchUiState.ScreenStatus.RESULT,
                        movies = movies,
                        series = series,
                        artists = artists
                    )
                }

            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        screenStatus = SearchUiState.ScreenStatus.FAILED,
                        errorMessage = ex.localizedMessage ?: "Unknown error"
                    )
                }
            }
        }
    }

    override fun onRecentSearchItemClicked(query: String) = onSearch(query)

    override fun onClearHistory() {
        viewModelScope.launch(dispatcher) {
            clearRecentSearchUseCase.clearAll()
            _uiState.update { it.copy(recentSearch = emptyList()) }
        }
    }

    override fun onRemoveHistoryItem(query: String) {
        viewModelScope.launch(dispatcher) {
            clearRecentSearchUseCase.removeQuery(query)
            val suggestions = getRecentSearchUseCase.getAll()
            _uiState.update { it.copy(recentSearch = suggestions) }
        }
    }

    override fun onBackClicked() {
        _uiState.update {
            if (it.screenStatus == SearchUiState.ScreenStatus.SEARCH) {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                    query = ""
                )
            } else if (it.screenStatus == SearchUiState.ScreenStatus.RESULT) {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.SEARCH,
                )
            } else {
                it
            }
        }
    }

}
