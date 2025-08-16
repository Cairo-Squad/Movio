package com.cairosquad.viewmodel.library

import android.util.Log
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LibraryScreenState, LibraryEffect>(LibraryScreenState()),
    LibraryInteractionListener {

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                updateState {
                    it.copy(isUserAuthed = authed)
                }
                loadScreenData()
            },
            onError = { throwable ->
                handleError(throwable) { copy(screenStatus = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun loadScreenData() {
        tryToCall(
            block = ::loadScreenState,
            onSuccess = {
                updateState {
                    it.copy(screenStatus = LibraryScreenState.SectionStatus.SUCCESS)
                }
            },
            onError = { throwable ->
                handleError(throwable) { copy(screenStatus = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    fun loadScreenState() {
        loadMoviesLists()
        loadSeriesLists()
        loadFavoriteMovies()
        loadFavoriteSeries()
        loadHistoryMovies()
        loadHistorySeries()
    }

    override fun onListsViewAllClick() {
        sendEffect(LibraryEffect.NavigateToLists)
    }

    override fun onFavoritesViewAllClick() {
        sendEffect(LibraryEffect.NavigateToFavorites)
    }

    override fun onHistoryViewAllClick() {
        sendEffect(LibraryEffect.NavigateToHistory)
    }

    override fun onLoginClicked() {
        sendEffect(LibraryEffect.NavigateToLogin)
    }

    override fun onRefresh() {
        loadScreenState()
    }

    override fun onListClicked(listId: Long, listName: String) {
        sendEffect(LibraryEffect.NavigateToListDetails(listId, listName))
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(LibraryEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(LibraryEffect.NavigateToSeriesDetails(seriesId))
    }

    // ----------------- Lists Section -----------------

    private fun loadMoviesLists() {
        updateState {it.copy(listsSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getMoviesLists(1) },
            onSuccess = ::onLoadingMoviesListsSuccess,
            onError = { throwable ->
                handleError(throwable) { copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingMoviesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            it.copy(
                movieLists = mediaLists.map { mediaList -> mediaList.toUiState() },
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun loadSeriesLists() {
        updateState {it.copy(listsSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getSeriesLists(1) },
            onSuccess = ::onLoadingSeriesListsSuccess,
            onError = { throwable ->
                handleError(throwable) { copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
            },
        )
    }

    private fun onLoadingSeriesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            it.copy(
                seriesLists = mediaLists.map { mediaList -> mediaList.toUiState() },
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    // ----------------- Favorites Section -----------------

    private fun loadFavoriteMovies() {
        updateState {it.copy(favoritesSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getFavoriteMovies(1) },
            onSuccess = ::onLoadingFavoriteMoviesSuccess,
            onError = { throwable ->
                handleError(throwable) { copy(favoritesSectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingFavoriteMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                favoriteMovies = movies.map { it.toUiState() },
                favoritesSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun loadFavoriteSeries() {
        updateState { it.copy(favoritesSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getFavoriteSeries(1) },
            onSuccess = ::onLoadingFavoriteSeriesSuccess,
            onError = { throwable ->
                handleError(throwable) { copy(favoritesSectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingFavoriteSeriesSuccess(series: List<Series>) {
        updateState {
            it.copy(
                favoriteSeries = series.map { it.toUiState() },
                favoritesSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    // ----------------- History Section -----------------

    private fun loadHistoryMovies() {
        updateState { it.copy(historySectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getHistoryMovies(1) },
            onSuccess = ::onLoadingHistoryMoviesSuccess,
            onError = { throwable ->
                handleError(throwable) { copy(historySectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingHistoryMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                historyMovies = movies.map { movie -> movie.toUiState() },
                historySectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun loadHistorySeries() {
        updateState {it.copy(historySectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getHistorySeries(1) },
            onSuccess = ::onLoadingHistorySeriesSuccess,
            onError = { throwable ->
                handleError(throwable) { copy(historySectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingHistorySeriesSuccess(series: List<Series>) {
        updateState {
            it.copy(
                historySeries = series.map { series -> series.toUiState() },
                historySectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    // ----------------- Helpers -----------------

    private fun LibraryScreenState.recalculateScreenStatus(): LibraryScreenState {
        val sectionStates = listOf(listsSectionState, favoritesSectionState, historySectionState)
        val newStatus = when {
            sectionStates.all { it == LibraryScreenState.SectionStatus.ERROR } -> LibraryScreenState.SectionStatus.ERROR
            sectionStates.any { it == LibraryScreenState.SectionStatus.LOADING } -> LibraryScreenState.SectionStatus.LOADING
            sectionStates.all { it == LibraryScreenState.SectionStatus.SUCCESS } -> LibraryScreenState.SectionStatus.SUCCESS
            else -> LibraryScreenState.SectionStatus.ERROR
        }
        return copy(screenStatus = newStatus)
    }

    private fun handleError(
        throwable: Throwable,
        updateSection: LibraryScreenState.() -> LibraryScreenState
    ) {
        Log.e("LibraryViewModel", throwable.toString())
        updateState {
            val updatedState = it.updateSection()
            updatedState.copy(
                errorStatus = handleLibraryException(throwable),
                isRefreshing = false
            ).recalculateScreenStatus()

        }

    }

    private fun handleLibraryException(e: Throwable): ErrorStatus {
        return when (e) {

            is MovioException ->{
                Log.e("movio","movio exception")
                exceptionToErrorStatus(e)
            }
            else -> {
                Log.e("movio" ,"unknown exception")
                ErrorStatus.UNKNOWN_ERROR
            }
        }
    }
}
