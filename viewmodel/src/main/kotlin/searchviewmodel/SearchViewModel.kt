package searchviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.usecase.GetSearchHistoryUseCase
import com.cairosquad.domain.usecase.SearchUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), SearchInteractionListener {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history.asStateFlow()

    private var searchJob: Job? = null

    override fun onQueryTextChanged(query: String) {
        viewModelScope.launch(dispatcher) {
            _history.value = getSearchHistoryUseCase.getAll(query)
        }
    }

    override fun onSearch(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        searchJob?.cancel()
        _uiState.value = SearchUiState.Loading

        searchJob = viewModelScope.launch(dispatcher) {
            try {
                val results = searchUseCase.searchMovies(query)

                _uiState.value =
                    if (results.isEmpty()) SearchUiState.Empty
                    else SearchUiState.Success(results)
            } catch (ex: Exception) {
                _uiState.value = SearchUiState.Error(ex.localizedMessage ?: "Unknown error")
            }
        }
    }

    override fun onHistoryItemClicked(query: String) {
        onSearch(query)
    }

    override fun onClearHistory() {
        viewModelScope.launch(dispatcher) {
            clearSearchHistoryUseCase.clearAll()
            _history.value = emptyList()
        }
    }

    override fun onRemoveHistoryItem(query: String) {
        viewModelScope.launch(dispatcher) {
            clearSearchHistoryUseCase.removeQuery(query)
            _history.value = getSearchHistoryUseCase.getAll("")//remove query
        }
    }
}