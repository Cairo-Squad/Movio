package com.cairosquad.viewmodel.searchviewmodel

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.GetRecentSearchUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val getExploreMoreUseCase: GetExploreMoreUseCase,
    private val getForYouUseCase: GetForYouUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SearchUiState, SearchUiEvent>(initialState = SearchUiState()),
    SearchInteractionListener {

    private var searchJob: Job? = null

    init {
        loadDiscoverMovies()
    }

    fun loadDiscoverMovies() = tryToCall(
        block = {
            val forYou = getForYouUseCase.getForYouMovies().map { it.toUiState() }
            val exploreMore = getExploreMoreUseCase.getExploreMoreMovies().map { it.toUiState() }
            forYou to exploreMore
        },
        onSuccess = { (forYou, exploreMore) ->
            updateState {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                    forYou = forYou,
                    exploreMore = exploreMore,
                    errorMessage = null
                )
            }
        },
        onError = { e ->
            val message = mapExceptionToMessage(e)
            updateState {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.FAILED,
                    errorMessage = message
                )
            }
            sendEvent(SearchUiEvent.ShowToast(message))
        }
    )

    override fun onQueryTextChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcher) {
            delay(300)
            val suggestions = getRecentSearchUseCase.getByQuery(query)
            updateState {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.SEARCH,
                    recentSearch = suggestions,
                    query = query,
                    errorMessage = null
                )
            }
        }
    }

    override fun onCancelSearch() {
        searchJob?.cancel()
        updateState {
            it.copy(
                screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                query = "",
                recentSearch = emptyList(),
                errorMessage = null
            )
        }
    }

    override fun onSearch(query: String) {
        searchJob?.cancel()
        updateState {
            it.copy(
                screenStatus = SearchUiState.ScreenStatus.LOADING,
                query = query,
                errorMessage = null
            )
        }

        searchJob = tryToCall(
            block = {
                val movies = searchUseCase.searchMovies(query).map { it.toUiState() }
                val series = searchUseCase.searchSeries(query).map { it.toUiState() }
                val artists = searchUseCase.searchArtists(query).map { it.toUiState() }
                Triple(movies, series, artists)
            },
            onSuccess = { (movies, series, artists) ->
                updateState {
                    it.copy(
                        screenStatus = SearchUiState.ScreenStatus.RESULT,
                        movies = movies,
                        series = series,
                        artists = artists,
                        errorMessage = null
                    )
                }
            },
            onError = { e ->
                val message = mapExceptionToMessage(e)
                updateState {
                    it.copy(
                        screenStatus = SearchUiState.ScreenStatus.FAILED,
                        errorMessage = message
                    )
                }
                sendEvent(SearchUiEvent.ShowToast(message))
            }
        )
    }

    override fun onRecentSearchItemClicked(query: String) = onSearch(query)

    override fun onClearHistory() {
        tryToCall(
            block = {
                clearRecentSearchUseCase.clearAll()
                emptyList<String>()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = suggestions, errorMessage = null) }
            },
            onError = { e ->
                val message = mapExceptionToMessage(e)
                updateState { it.copy(errorMessage = message) }
                sendEvent(SearchUiEvent.ShowToast(message))
            }
        )
    }

    override fun onRemoveHistoryItem(query: String) {
        tryToCall(
            block = {
                clearRecentSearchUseCase.removeQuery(query)
                getRecentSearchUseCase.getAll()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = suggestions, errorMessage = null) }
            },
            onError = { e ->
                val message = mapExceptionToMessage(e)
                updateState { it.copy(errorMessage = mapExceptionToMessage(e)) }
                sendEvent(SearchUiEvent.ShowToast(message))
            }
        )
    }


    override fun onBackClicked() {
        updateState {
            when (it.screenStatus) {
                SearchUiState.ScreenStatus.SEARCH -> it.copy(
                    screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                    query = "",
                    recentSearch = emptyList(),
                    errorMessage = null
                )

                SearchUiState.ScreenStatus.RESULT -> it.copy(
                    screenStatus = SearchUiState.ScreenStatus.SEARCH,
                    errorMessage = null
                )

                else -> it
            }
        }
    }

    private fun mapExceptionToMessage(e: Throwable): String = when (e) {
        is IOException -> "Network error. Please check your connection."
        else -> e.localizedMessage ?: "An unexpected error occurred."
    }
}