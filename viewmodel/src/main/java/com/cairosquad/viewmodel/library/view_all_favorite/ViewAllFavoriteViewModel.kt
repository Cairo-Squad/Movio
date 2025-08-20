package com.cairosquad.viewmodel.library.view_all_favorite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewAllFavoriteViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
) : BaseViewModel<ViewAllFavoriteScreenState, ViewAllFavoriteEffect>(ViewAllFavoriteScreenState()),
    ViewAllFavoriteInteractionListener {

    init {
        getFavorites()
    }

    private fun getFavorites() {
        getFavoriteMovies()
        getFavoriteSeries()
    }

    private fun getFavoriteSeries() {
        tryToCall(
            block = { accountUseCase.getFavoriteSeries(FIRST_PAGE) },
            onSuccess = ::handleSeriesSuccess,
            onError = ::handleFavoritesError
        )
    }

    private fun getFavoriteMovies() {
        tryToCall(
            block = { accountUseCase.getFavoriteMovies(FIRST_PAGE) },
            onSuccess = ::handleMoviesSuccess,
            onError = ::handleFavoritesError
        )
    }

    override fun onBackClick() = sendEffect(ViewAllFavoriteEffect.OnNavigateBack)

    override fun onMovieClick(movieId: Long) = sendEffect(ViewAllFavoriteEffect.OnMovieClicked(movieId))

    override fun onSeriesClick(seriesId: Long) = sendEffect(ViewAllFavoriteEffect.OnSeriesClicked(seriesId))

    override fun onMovieDelete(movieId: Long) {
        tryToCall(
            onStart = { handleMovieDeleteStart(movieId) },
            block = { accountUseCase.removeMovieFromFavorite(movieId) },
            onSuccess = { handleMovieDeleteSuccess() },
            onError = { handleMovieDeleteError() },
            onEnd = { handleDeleteEnd() }
        )
    }

    override fun onSeriesDelete(seriesId: Long) {
        tryToCall(
            onStart = { handleSeriesDeleteStart(seriesId) },
            block = { accountUseCase.removeSeriesFromFavorite(seriesId) },
            onSuccess = { handleSeriesDeleteSuccess() },
            onError = { handleSeriesDeleteError() },
            onEnd = { handleDeleteEnd() }
        )
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            getFavorites()
            delay(REFRESH_DELAY)
            updateState { it.copy(isRefreshing = true) }
        }
    }

    override fun onUndoClick() {
        tryToCall(
            block = { handleUndoBlock() },
            onSuccess = { handleUndoSuccess() },
            onError = { handleUndoError() },
            onEnd = { handleDeleteEnd() }
        )
    }

    private fun handleSeriesSuccess(seriesList: List<Series>) {
        val filteredSeries = seriesList.filterNot { it.id in screenState.value.deletedSeriesIds }
            .map { it.toUiState() }
        updateState {
            it.copy(
                series = filteredSeries,
                screenStatus = ViewAllFavoriteScreenState.SectionStatus.SUCCESS
            )
        }
    }

    private fun handleMoviesSuccess(movies: List<Movie>) {
        val filteredMovies = movies.filterNot { it.id in screenState.value.deletedMoviesIds }
            .map { it.toUiState() }
        updateState {
            it.copy(
                movies = filteredMovies,
                screenStatus = ViewAllFavoriteScreenState.SectionStatus.SUCCESS
            )
        }
    }

    private fun handleFavoritesError(throwable: Throwable) {
        updateScreenStatus(ViewAllFavoriteScreenState.SectionStatus.ERROR)
        updateErrorStatus(throwable)
    }

    private fun handleMovieDeleteStart(movieId: Long) {
        updateState { state ->
            state.copy(
                movies = state.movies.filter { it.id != movieId },
                deletedMoviesIds = state.deletedMoviesIds + movieId,
                deletedItems = state.deletedItems + "$movieId, movie"
            )
        }
    }

    private fun handleMovieDeleteSuccess() {
        updateState {
            it.copy(
                showSnackBar = true,
                isProcessSuccess = true,
                snackMessageId = R.string.movie_favorite_remove_success
            )
        }
    }

    private fun handleMovieDeleteError() {
        updateState {
            it.copy(
                showSnackBar = true,
                isProcessSuccess = false,
                snackMessageId = R.string.movie_favorite_remove_fail
            )
        }
    }

    private fun handleSeriesDeleteStart(seriesId: Long) {
        updateState { state ->
            state.copy(
                series = state.series.filter { it.id != seriesId },
                deletedSeriesIds = state.deletedSeriesIds + seriesId,
                deletedItems = state.deletedItems + "$seriesId, series"
            )
        }
    }

    private fun handleSeriesDeleteSuccess() {
        updateState {
            it.copy(
                showSnackBar = true,
                isProcessSuccess = true,
                snackMessageId = R.string.series_favorite_remove_success
            )
        }
    }

    private fun handleSeriesDeleteError() {
        updateState {
            it.copy(
                showSnackBar = true,
                isProcessSuccess = false,
                snackMessageId = R.string.series_favorite_remove_fail
            )
        }
    }

    private suspend fun handleDeleteEnd() {
        delay(SNACKBAR_DURATION)
        updateState { it.copy(showSnackBar = false) }
    }

    private suspend fun handleUndoBlock() {
        val item = screenState.value.deletedItems.last().split(ITEM_DELIMITER)
        when (item[FIRST_PAGE]) {
            TYPE_MOVIE -> accountUseCase.addMovieToFavorite(item[0].toLong())
            TYPE_SERIES -> accountUseCase.addSeriesToFavorite(item[0].toLong())
        }
    }

    private fun handleUndoSuccess() {
        val item = screenState.value.deletedItems.last().split(ITEM_DELIMITER)
        when (item[FIRST_PAGE]) {
            TYPE_MOVIE -> {
                updateState {
                    it.copy(
                        deletedItems = it.deletedItems.dropLast(FIRST_PAGE),
                        deletedMoviesIds = it.deletedMoviesIds.dropLast(FIRST_PAGE),
                        showSnackBar = false
                    )
                }
                getFavoriteMovies()
            }
            TYPE_SERIES -> {
                updateState {
                    it.copy(
                        deletedItems = it.deletedItems.dropLast(FIRST_PAGE),
                        deletedSeriesIds = it.deletedSeriesIds.dropLast(FIRST_PAGE),
                        showSnackBar = false
                    )
                }
                getFavoriteSeries()
            }
        }
    }

    private fun handleUndoError() {
        updateState {
            it.copy(
                showSnackBar = true,
                isProcessSuccess = false,
                snackMessageId = R.string.favorite_restore_fail
            )
        }
    }

    private fun updateScreenStatus(status: ViewAllFavoriteScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    private fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ViewAllFavoriteScreenState.SectionStatus.ERROR) }
    }

    private fun <T : Any, R : Any> cacheMappedPagingData(
        scope: CoroutineScope,
        fetch: () -> Flow<PagingData<T>>,
        map: (T) -> R
    ): Flow<PagingData<R>> = fetch()
        .map { pagingData -> pagingData.map(map) }
        .cachedIn(scope)

    private fun handleError(
        throwable: Throwable,
        updateSection: ViewAllFavoriteScreenState.() -> ViewAllFavoriteScreenState
    ) {
        updateState {
            it.updateSection().copy(
                errorStatus = handleException(throwable),
                isRefreshing = false
            )
        }
    }

    private fun handleException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> exceptionToErrorStatus(e)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }

     companion object {
        private const val FIRST_PAGE = 1
        private const val REFRESH_DELAY = 500L
        private const val SNACKBAR_DURATION = 2000L
        private const val ITEM_DELIMITER = ", "
        private const val TYPE_MOVIE = "movie"
        private const val TYPE_SERIES = "series"
    }
}
