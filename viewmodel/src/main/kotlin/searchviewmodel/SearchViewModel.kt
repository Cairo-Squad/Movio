package searchviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetSearchHistoryUseCase
import com.cairosquad.domain.usecase.GetForYouUseCase
import com.cairosquad.domain.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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
    private val getExploreMoreUseCase: GetExploreMoreUseCase,
    private val getForYouUseCase: GetForYouUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), SearchInteractionListener {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun loadDiscoverMovies() {
        viewModelScope.launch(dispatcher) {
            try {
                val forYou = getForYouUseCase.getForYouMovies().map { it.toUiState() }
                val exploreMore = getExploreMoreUseCase.getExploreMoreMovies().map { it.toUiState() }

                _uiState.value = SearchUiState(
                    isIdle = false,
                    forYou = forYou,
                    exploreMore = exploreMore
                )
            } catch (e: Exception) {
                _uiState.value = SearchUiState(errorMessage = "Failed to load discover content.")
            }
        }
    }

    override fun onQueryTextChanged(query: String) {
        viewModelScope.launch(dispatcher) {
            val suggestions = getSearchHistoryUseCase.getByQuery(query)
            _uiState.value = SearchUiState(
                isIdle = false,
                searchSuggestions = suggestions
            )
        }
    }

    override fun onSearch(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState(isIdle = true)
            return
        }

        searchJob?.cancel()
        _uiState.value = SearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(dispatcher) {
            try {
                val movies = searchUseCase.searchMovies(query)
                val series = searchUseCase.searchSeries(query)
                val artists = searchUseCase.searchArtists(query)

                val topResult = movies.firstOrNull()?.toUiState()

                if (movies.isEmpty() && series.isEmpty() && artists.isEmpty()) {
                    _uiState.value = SearchUiState(isEmpty = true)
                } else {
                    _uiState.value = SearchUiState(
                        topResult = topResult,
                        movies = movies.map { it.toUiState() },
                        series = series.map { it.toUiState() },
                        artists = artists.map { it.toUiState() }
                    )
                }
            } catch (ex: Exception) {
                _uiState.value = SearchUiState(errorMessage = ex.localizedMessage ?: "Unknown error")
            }
        }
    }

    override fun onHistoryItemClicked(query: String) {
        onSearch(query)
    }

    override fun onClearHistory() {
        viewModelScope.launch(dispatcher) {
            clearSearchHistoryUseCase.clearAll()
            _uiState.value = SearchUiState(searchSuggestions = emptyList())
        }
    }

    override fun onRemoveHistoryItem(query: String) {
        viewModelScope.launch(dispatcher) {
            clearSearchHistoryUseCase.removeQuery(query)
            val suggestions = getSearchHistoryUseCase.getAll()
            _uiState.value = SearchUiState(searchSuggestions = suggestions)
        }
    }
}


/* ---------- MAPPERS ---------- */
fun Movie.toUiState() = MovieUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath
)

fun Artist.toUiState() = ArtistUiState(
    id = id,
    name = name,
    photoPath = photoPath
)

fun Series.toUiState() = SeriesUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath
)
