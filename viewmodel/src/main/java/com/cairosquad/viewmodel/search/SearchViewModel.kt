package com.cairosquad.viewmodel.search

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.search.exception.MovioException
import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetSuggestedMoviesUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val getLocalSearchHistoryUseCase: GetLocalSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val getSuggestedMoviesUseCase: GetSuggestedMoviesUseCase,
    private val getPersonalizedMoviesUseCase: GetPersonalizedMoviesUseCase,
) : BaseViewModel<SearchScreenState, SearchEffect>(initialState = SearchScreenState()),
    SearchInteractionListener {

    private var searchJob: Job? = null

    init {
        loadDiscoverMovies()
    }

    fun loadDiscoverMovies() = tryToCall(
        block = {
            updateState {
                it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.LOADING,
                )
            }
            val forYou = getPersonalizedMoviesUseCase.getPersonalizedMovies().map { it.toUiState() }
            val exploreMore = getSuggestedMoviesUseCase.getSuggestedMovies().map { it.toUiState() }
            forYou to exploreMore
        },
        onSuccess = { (forYou, exploreMore) ->
            updateState {
                it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.EXPLORE,
                    forYou = forYou,
                    exploreMore = exploreMore,
                    errorStatus = null
                )
            }
        },
        onError = { e ->
            updateState {
                it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.FAILED,
                    errorStatus = handleSearchException(e)
                )
            }
        },
        dispatcher = Dispatchers.IO
    )

    override fun onQueryTextChanged(query: String) {
        updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.SEARCH,
                query = query
            )
        }
        searchJob?.cancel()
        searchJob = tryToCall(
            block = {
                delay(300)
                getLocalSearchHistoryUseCase.getByQuery(query)
            },
            onSuccess = { suggestions ->
                updateState {
                    it.copy(
                        recentSearch = suggestions
                    )
                }
            },
            onError = {},
            dispatcher = Dispatchers.IO
        )
    }

    override fun onCancelSearch() {
        searchJob?.cancel()
        updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.EXPLORE,
                query = "",
                recentSearch = emptyList(),
                errorStatus = null
            )
        }
    }

    override fun onSearch(query: String) {
        updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.LOADING,
                query = query,
                errorStatus = null
            )
        }

        if (query.isBlank()) {
            updateState {
                it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.SEARCH,
                )
            }
        } else {
            tryToCall(
                block = {
                    val movies = searchUseCase.getMovies(query).map { it.toUiState() }
                    val series = searchUseCase.getSeries(query).map { it.toUiState() }
                    val artists = searchUseCase.getArtists(query).map { it.toUiState() }
                    Triple(movies, series, artists)
                },
                onSuccess = { (movies, series, artists) ->
                    updateState {
                        it.copy(
                            screenStatus = SearchScreenState.ScreenStatus.RESULT,
                            movies = movies,
                            series = series,
                            artists = artists,
                            errorStatus = null
                        )
                    }
                },
                onError = { e ->
                    updateState {
                        it.copy(
                            screenStatus = SearchScreenState.ScreenStatus.FAILED,
                            errorStatus = handleSearchException(e)
                        )
                    }
                },
                dispatcher = Dispatchers.IO
            )
        }
    }

    override fun onRecentSearchItemClicked(query: String) = onSearch(query)

    override fun onClearHistory() {
        tryToCall(
            block = {
                clearSearchHistoryUseCase.clearAllHistory()
                emptyList<String>()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = suggestions, errorStatus = null) }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = SearchScreenState.ScreenStatus.FAILED,
                        errorStatus = handleSearchException(e)
                    )
                }
            },
            dispatcher = Dispatchers.IO
        )
    }

    override fun onRemoveHistoryItem(query: String) {
        tryToCall(
            block = {
                clearSearchHistoryUseCase.removeQueryFromHistory(query)
            },
            onSuccess = {
                updateState {
                    it.copy(
                        recentSearch = it.recentSearch.filterNot { q -> q == query },
                        errorStatus = null
                    )
                }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = SearchScreenState.ScreenStatus.FAILED,
                        errorStatus = handleSearchException(e)
                    )
                }
            },
            dispatcher = Dispatchers.IO
        )
    }


    override fun onBackClicked() {
        updateState {
            when (it.screenStatus) {
                SearchScreenState.ScreenStatus.SEARCH -> it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.EXPLORE,
                    query = "",
                    recentSearch = emptyList(),
                )

                SearchScreenState.ScreenStatus.RESULT -> it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.SEARCH,
                )

                else -> it
            }
        }
    }

    override fun onClickSearchTextField() {
        tryToCall(
            block = {
                if (screenState.value.query.isBlank()) {
                    getLocalSearchHistoryUseCase.getAll()
                } else {
                    getLocalSearchHistoryUseCase.getByQuery(screenState.value.query)
                }
            },
            onSuccess = { suggestions ->
                updateState {
                    it.copy(
                        screenStatus = SearchScreenState.ScreenStatus.SEARCH,
                    )
                }
                updateState { it.copy(recentSearch = suggestions) }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = SearchScreenState.ScreenStatus.SEARCH,
                    )
                }
            }
        )
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            delay(500L)
            updateState { it.copy(isRefreshing = false) }
        }
        if (screenState.value.query.isBlank()) loadDiscoverMovies()
        else onSearch(screenState.value.query)
    }

    private fun handleSearchException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}