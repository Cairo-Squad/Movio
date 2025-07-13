package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetSuggestedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.IOException

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val getRecentSearchUseCase: GetLocalSearchHistoryUseCase,
    private val clearRecentSearchUseCase: ClearSearchHistoryUseCase,
    private val getExploreMoreUseCase: GetSuggestedMoviesUseCase,
    private val getForYouUseCase: GetPersonalizedMoviesUseCase,
) : BaseViewModel<SearchUiState, SearchUiEvent>(initialState = SearchUiState()),
    SearchInteractionListener {

    private var searchJob: Job? = null

    init {
        loadDiscoverMovies()
    }

    fun loadDiscoverMovies() = tryToCall(
        block = {
            val forYou = getForYouUseCase.getPersonalizedMovies().map { it.toUiState() }
            val exploreMore = getExploreMoreUseCase.getSuggestedMovies().map { it.toUiState() }
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
        },
        dispatcher = Dispatchers.IO
    )

    override fun onQueryTextChanged(query: String) {
        updateState {
            it.copy(
                screenStatus = SearchUiState.ScreenStatus.SEARCH,
                query = query
            )
        }
        searchJob?.cancel()
        searchJob = tryToCall(
            block = {
                delay(300)
                getRecentSearchUseCase.getByQuery(query)
            },
            onSuccess = { suggestions ->
                updateState {
                    it.copy(
                        recentSearch = suggestions
                    )
                }
            },
            onError = { },
            dispatcher = Dispatchers.IO
        )
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

        if (query.isBlank()) {
            updateState {
                it.copy(
                    screenStatus = SearchUiState.ScreenStatus.SEARCH,
                )
            }
        } else {
            searchJob = tryToCall(
                block = {
                    val movies = searchUseCase.getMovies(query).map { it.toUiState() }
                    val series = searchUseCase.getSeries(query).map { it.toUiState() }
                    val artists = searchUseCase.getArtists(query).map { it.toUiState() }
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
                },
                dispatcher = Dispatchers.IO
            )
        }
    }

    override fun onRecentSearchItemClicked(query: String) = onSearch(query)

    override fun onClearHistory() {
        tryToCall(
            block = {
                clearRecentSearchUseCase.clearAllHistory()
                emptyList<String>()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = suggestions, errorMessage = null) }
            },
            onError = { e ->
                val message = mapExceptionToMessage(e)
                updateState { it.copy(errorMessage = message) }
                sendEvent(SearchUiEvent.ShowToast(message))
            },
            dispatcher = Dispatchers.IO
        )
    }

    override fun onRemoveHistoryItem(query: String) {
        tryToCall(
            block = {
                clearRecentSearchUseCase.removeQueryFromHistory(query)
                getRecentSearchUseCase.getAll()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = suggestions, errorMessage = null) }
            },
            onError = { e ->
                val message = mapExceptionToMessage(e)
                updateState { it.copy(errorMessage = mapExceptionToMessage(e)) }
                sendEvent(SearchUiEvent.ShowToast(message))
            },
            dispatcher = Dispatchers.IO
        )
    }


    override fun onBackClicked() {
        updateState {
            when (it.screenStatus) {
                SearchUiState.ScreenStatus.SEARCH -> it.copy(
                    screenStatus = SearchUiState.ScreenStatus.EXPLORE,
                    query = "",
                    recentSearch = emptyList(),
                )

                SearchUiState.ScreenStatus.RESULT -> it.copy(
                    screenStatus = SearchUiState.ScreenStatus.SEARCH,
                )

                else -> it
            }
        }
    }

    override fun onClickSearchTextField() {
        tryToCall(
            block = {
                getRecentSearchUseCase.getAll()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = suggestions, errorMessage = null) }
            },
            onError = { }
        )

        searchJob?.cancel()
        updateState {
            it.copy(
                screenStatus = SearchUiState.ScreenStatus.SEARCH,
            )
        }
    }

    private fun mapExceptionToMessage(e: Throwable): String = when (e) {
        is IOException -> "Network error. Please check your connection."
        else -> e.localizedMessage ?: "An unexpected error occurred."
    }
}