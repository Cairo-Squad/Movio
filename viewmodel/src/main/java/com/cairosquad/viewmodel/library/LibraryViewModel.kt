package com.cairosquad.viewmodel.library
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
            onSuccess = ::onUserLoggedInSuccess,
            onError = ::onUserLoggedInError
        )
    }

    private fun onUserLoggedInSuccess(authed: Boolean) {
        updateState { it.copy(isUserAuthed = authed) }
        loadScreenData()
    }

    private fun onUserLoggedInError(e: Throwable) {
        handleError(e) { copy(screenStatus = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun loadScreenData() {
        tryToCall(
            block = ::loadScreenState,
            onSuccess = ::onLoadScreenDataSuccess,
            onError = ::onLoadScreenDataError
        )
    }

    private fun onLoadScreenDataSuccess(@Suppress("UNUSED_PARAMETER") unit: Unit) {
        updateState { it.copy(screenStatus = LibraryScreenState.SectionStatus.SUCCESS) }
    }

    private fun onLoadScreenDataError(e: Throwable) {
        handleError(e) { copy(screenStatus = LibraryScreenState.SectionStatus.ERROR) }
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

    override fun onLoginClick() {
        sendEffect(LibraryEffect.NavigateToLogin)
    }

    override fun onRefresh() {
        loadScreenState()
    }

    override fun onListClick(listId: Long, listName: String) {
        sendEffect(LibraryEffect.NavigateToListDetails(listId, listName))
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(LibraryEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(LibraryEffect.NavigateToSeriesDetails(seriesId))
    }

    private fun loadMoviesLists() {
        updateState { it.copy(listsSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getMoviesLists(1) },
            onSuccess = ::onLoadingMoviesListsSuccess,
            onError = ::onLoadingMoviesListsError
        )
    }

    private fun onLoadingMoviesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            it.copy(
                movieLists = mediaLists.map(MediaList::toUiState),
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun onLoadingMoviesListsError(e: Throwable) {
        handleError(e) { copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun loadSeriesLists() {
        updateState { it.copy(listsSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getSeriesLists(1) },
            onSuccess = ::onLoadingSeriesListsSuccess,
            onError = ::onLoadingSeriesListsError
        )
    }

    private fun onLoadingSeriesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            it.copy(
                seriesLists = mediaLists.map(MediaList::toUiState),
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun onLoadingSeriesListsError(e: Throwable) {
        handleError(e) { copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun loadFavoriteMovies() {
        updateState { it.copy(favoritesSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getFavoriteMovies(1) },
            onSuccess = ::onLoadingFavoriteMoviesSuccess,
            onError = ::onLoadingFavoriteMoviesError
        )
    }

    private fun onLoadingFavoriteMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                favoriteMovies = movies.map(Movie::toUiState),
                favoritesSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun onLoadingFavoriteMoviesError(e: Throwable) {
        handleError(e) { copy(favoritesSectionState = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun loadFavoriteSeries() {
        updateState { it.copy(favoritesSectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getFavoriteSeries(1) },
            onSuccess = ::onLoadingFavoriteSeriesSuccess,
            onError = ::onLoadingFavoriteSeriesError
        )
    }

    private fun onLoadingFavoriteSeriesSuccess(series: List<Series>) {
        updateState {
            it.copy(
                favoriteSeries = series.map(Series::toUiState),
                favoritesSectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun onLoadingFavoriteSeriesError(e: Throwable) {
        handleError(e) { copy(favoritesSectionState = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun loadHistoryMovies() {
        updateState { it.copy(historySectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getHistoryMovies(1) },
            onSuccess = ::onLoadingHistoryMoviesSuccess,
            onError = ::onLoadingHistoryMoviesError
        )
    }

    private fun onLoadingHistoryMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                historyMovies = movies.map(Movie::toUiState),
                historySectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun onLoadingHistoryMoviesError(e: Throwable) {
        handleError(e) { copy(historySectionState = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun loadHistorySeries() {
        updateState { it.copy(historySectionState = LibraryScreenState.SectionStatus.LOADING) }
        tryToCall(
            block = { accountUseCase.getHistorySeries(1) },
            onSuccess = ::onLoadingHistorySeriesSuccess,
            onError = ::onLoadingHistorySeriesError
        )
    }

    private fun onLoadingHistorySeriesSuccess(series: List<Series>) {
        updateState {
            it.copy(
                historySeries = series.map(Series::toUiState),
                historySectionState = LibraryScreenState.SectionStatus.SUCCESS
            ).recalculateScreenStatus()
        }
    }

    private fun onLoadingHistorySeriesError(e: Throwable) {
        handleError(e) { copy(historySectionState = LibraryScreenState.SectionStatus.ERROR) }
    }

    private fun LibraryScreenState.recalculateScreenStatus(): LibraryScreenState {
        val sectionStates = listOf(listsSectionState, favoritesSectionState, historySectionState)
        val newStatus = when {
            sectionStates.all { it == LibraryScreenState.SectionStatus.ERROR } -> LibraryScreenState.SectionStatus.ERROR
            sectionStates.any { it == LibraryScreenState.SectionStatus.LOADING } -> LibraryScreenState.SectionStatus.LOADING
            sectionStates.any { it == LibraryScreenState.SectionStatus.SUCCESS } -> LibraryScreenState.SectionStatus.SUCCESS
            else -> LibraryScreenState.SectionStatus.ERROR
        }
        return copy(screenStatus = newStatus)
    }

    private fun handleError(
        throwable: Throwable,
        updateSection: LibraryScreenState.() -> LibraryScreenState
    ) {
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
            is MovioException -> exceptionToErrorStatus(e)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}