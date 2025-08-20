package com.cairosquad.viewmodel.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.GetSearchRecommendationUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSearchHistoryUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState.ArtistUiState
import com.cairosquad.viewmodel.search.SearchScreenState.MovieUiState
import com.cairosquad.viewmodel.search.SearchScreenState.SeriesUiState
import com.cairosquad.viewmodel.search.paging.SearchPager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPager: SearchPager,
    private val manageSearchHistoryUseCase: ManageSearchHistoryUseCase,
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val getSearchRecommendationUseCase: GetSearchRecommendationUseCase
) : BaseViewModel<SearchScreenState, SearchEffect>(initialState = SearchScreenState()),
    SearchInteractionListener {

    private var searchJob: Job? = null

    init {
        loadDiscoverMovies()
    }

    fun loadDiscoverMovies() {
        setLoading()
        getPersonalizedMovies()
        getSuggestedMovies()
    }

    fun getPersonalizedMovies() {
        tryToCall(
            onStart = ::onFetchStart,
            block = { manageMoviesUseCase.getPersonalizedMovies(1).map { it.toUiState() } },
            onSuccess = ::onPersonalizedMoviesSuccess,
            onError = ::onFetchError
        )
    }

    fun getSuggestedMovies() {
        tryToCall(
            onStart = ::onFetchStart,
            block = { manageMoviesUseCase.getSuggestedMovies().map { it.toUiState() } },
            onSuccess = ::onSuggestedMoviesSuccess,
            onError = ::onFetchError
        )
    }

    override fun onQueryTextChanged(query: String) {
        if (query == screenState.value.query) return
        if (query.trim() != screenState.value.query.trim()) debounceSearchSuggestions(query.trim())
        enterSearchMode(query)
    }

    private fun enterSearchMode(query: String) {
        updateState { it.copy(query = query) }
    }

    private fun debounceSearchSuggestions(query: String) {
        searchJob?.cancel()
        searchJob = tryToCall(
            block = {
                delay(300)
                val recent = manageSearchHistoryUseCase.getByQuery(query)
                val recommendations = getSearchRecommendationUseCase(query)
                Pair(recent, recommendations)
            },
            onSuccess = ::updateSuggestions,
            onError = {},
            dispatcher = Dispatchers.IO
        )
    }

    private fun updateSuggestions(suggestions: Pair<List<String>, List<String>>) {
        val (recent, recommendations) = suggestions
        updateState { it.copy(recentSearch = recent, searchRecommendation = recommendations) }
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
        val query = screenState.value.query.trim()
        if (query.isBlank()) return

        setLoading()

        tryToCall(
            onStart = ::onFetchStart,
            block = {
                val movies = cacheMappedPagingData(query, searchPager::movies) { it.toUiState() }
                val series = cacheMappedPagingData(query, searchPager::series) { it.toUiState() }
                val artists = cacheMappedPagingData(query, searchPager::artists) { it.toUiState() }
                Triple(movies, series, artists)
            },
            onSuccess = ::onSearchSuccess,
            onError = ::onFetchError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onSearchSuccess(result: Triple<Flow<PagingData<MovieUiState>>, Flow<PagingData<SeriesUiState>>, Flow<PagingData<ArtistUiState>>>) {
        val (movies, series, artists) = result
        updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.RESULT,
                movies = movies,
                series = series,
                artists = artists,
                errorStatus = null
            )
        }
    }

    private fun <T : Any, R : Any> cacheMappedPagingData(
        query: String,
        fetch: (String) -> Flow<PagingData<T>>,
        map: (T) -> R
    ): Flow<PagingData<R>> {
        return fetch(query).map { it.map(map) }.cachedIn(viewModelScope)
    }

    override fun onRecentSearchItemClick(query: String) {
        updateState { it.copy(query = query) }
        onSearch()
    }

    override fun onClearHistory() {
        tryToCall(
            block = { manageSearchHistoryUseCase.clearAllHistory() },
            onSuccess = { updateState { it.copy(recentSearch = emptyList(), errorStatus = null) } },
            onError = ::onFetchError,
            dispatcher = Dispatchers.IO
        )
    }

    override fun onRemoveHistoryItem(query: String) {
        tryToCall(
            block = { manageSearchHistoryUseCase.removeQueryFromHistory(query) },
            onSuccess = { updateState { it.copy(recentSearch = it.recentSearch.filterNot { item -> item == query }, errorStatus = null) } },
            onError = ::onFetchError,
            dispatcher = Dispatchers.IO
        )
    }

    override fun onBackClick() {
        updateState {
            when (it.screenStatus) {
                SearchScreenState.ScreenStatus.SEARCH -> it.copy(
                    screenStatus = SearchScreenState.ScreenStatus.EXPLORE,
                    query = "",
                    recentSearch = emptyList()
                )
                SearchScreenState.ScreenStatus.RESULT -> it.copy(screenStatus = SearchScreenState.ScreenStatus.SEARCH)
                else -> it
            }
        }
    }

    override fun onClickSearchTextField() {
        val query = screenState.value.query
        val prev = screenState.value.recentSearch

        tryToCall(
            block = {
                val recent = if (query.isBlank()) manageSearchHistoryUseCase.getAll()
                else manageSearchHistoryUseCase.getByQuery(query)
                val recommendations = getSearchRecommendationUseCase(query)
                Pair(recent, recommendations)
            },
            onSuccess = ::updateSuggestions,
            onError = { updateState { it.copy(recentSearch = prev) } }
        )
        updateState { it.copy(screenStatus = SearchScreenState.ScreenStatus.SEARCH) }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            delay(500)
            updateState { it.copy(isRefreshing = false) }
        }
        if (screenState.value.query.isBlank()) loadDiscoverMovies() else onSearch()
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(SearchEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(SearchEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onArtistClick(artistId: Long) {
        sendEffect(SearchEffect.NavigateToArtistDetails(artistId))
    }

    override fun onSeeAllForYouClick() {
        sendEffect(SearchEffect.NavigateToSeeAllForYouScreen)
    }

    override fun onTabSelected(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
    }

    override fun onTabPagingError(error: Throwable) {
        val status = handleSearchException(error)
        updateState { it.copy(errorStatus = status) }
    }

    private fun setLoading() {
        updateState { it.copy(screenStatus = SearchScreenState.ScreenStatus.LOADING) }
    }

    private fun onFetchStart() {
        updateState { it.copy(screenStatus = SearchScreenState.ScreenStatus.LOADING, errorStatus = null) }
    }

    private fun onPersonalizedMoviesSuccess(forYou: List<MovieUiState>) {
        updateState { it.copy(forYou = forYou, screenStatus = SearchScreenState.ScreenStatus.EXPLORE) }
    }

    private fun onSuggestedMoviesSuccess(exploreMore: List<MovieUiState>) {
        updateState {
            it.copy(screenStatus = SearchScreenState.ScreenStatus.EXPLORE, exploreMore = exploreMore, errorStatus = null)
        }
    }

    private fun onFetchError(e: Throwable) {
        updateState { it.copy(screenStatus = SearchScreenState.ScreenStatus.FAILED, errorStatus = handleSearchException(e)) }
    }

    private fun handleSearchException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> exceptionToErrorStatus(e)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}