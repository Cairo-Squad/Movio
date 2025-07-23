package com.cairosquad.viewmodel.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.movies.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetSuggestedMoviesUseCase
import com.cairosquad.domain.usecase.search.ClearSearchHistoryUseCase
import com.cairosquad.domain.usecase.search.GetLocalSearchHistoryUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.search.paging.SearchPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchPager: SearchPager,
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
            val forYou = getPersonalizedMoviesUseCase.getPersonalizedMovies(1).map { it.toUiState() }
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

    override fun onSearch() {

        val query = screenState.value.query

        if (query.isBlank()) return

        updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.LOADING,
                errorStatus = null
            )
        }

        tryToCall(
            block = {
                val movies = cacheMappedPagingData(
                    query = query,
                    scope = viewModelScope,
                    fetch = { searchPager.movies(it) },
                    map = { it.toUiState() }
                )
                val series = cacheMappedPagingData(
                    query = query,
                    scope = viewModelScope,
                    fetch = { searchPager.series(it) },
                    map = { it.toUiState() }
                )
                val artists = cacheMappedPagingData(
                    query = query,
                    scope = viewModelScope,
                    fetch = { searchPager.artists(it) },
                    map = { it.toUiState() }
                )
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
                sendEffect(SearchEffect.HideKeyboard)
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

    private fun <T : Any, R : Any> cacheMappedPagingData(
        query: String,
        scope: CoroutineScope,
        fetch: (String) -> Flow<PagingData<T>>,
        map: (T) -> R
    ): Flow<PagingData<R>> = fetch(query)
        .map { pagingData -> pagingData.map(map) }
        .cachedIn(scope)

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(query = query) }
        onSearch()
    }

    override fun onClearHistory() {
        tryToCall(
            block = {
                clearSearchHistoryUseCase.clearAllHistory()
            },
            onSuccess = { suggestions ->
                updateState { it.copy(recentSearch = emptyList(), errorStatus = null) }
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
        val previousSearch = screenState.value.recentSearch
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
                        recentSearch = previousSearch
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
        else onSearch()
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(SearchEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(SearchEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onArtistClicked(artistId: Long) {
        sendEffect(SearchEffect.NavigateToArtistDetails(artistId))
    }

    override fun onSeeAllForYouClicked() {
        sendEffect(SearchEffect.NavigateToSeeAllForYouScreen)
    }

    override fun onTabSelected(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
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