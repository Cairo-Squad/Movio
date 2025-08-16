package com.cairosquad.viewmodel.library.view_all_history

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewAllHistoryViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase,
) : BaseViewModel<ViewAllHistoryScreenState, ViewAllHistoryEffect>(ViewAllHistoryScreenState()),
    ViewAllHistoryInteractionListener {

    init {
        loadHistory()
    }

    private fun loadHistory() {
        loadHistoryMovies()
        loadHistorySeries()
    }

    private fun loadHistoryMovies() {
        tryToCall(
            block = { accountUseCase.getHistoryMovies(1) },
            onSuccess = ::onLoadHistoryMoviesSuccess,
            onError = ::onLoadHistoryMoviesError
        )
    }

    private fun onLoadHistoryMoviesSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                movies = movies.map { movie -> movie.toUiState() },
                screenStatus = ViewAllHistoryScreenState.SectionStatus.SUCCESS
            )
        }
        updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.SUCCESS)
    }

    private fun onLoadHistoryMoviesError(throwable: Throwable) {
        updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.ERROR)
        updateErrorStatus(throwable)
    }


    private fun loadHistorySeries() {
        tryToCall(
            block = { accountUseCase.getHistorySeries(1) },
            onSuccess = ::onLoadHistorySeriesSuccess,
            onError = ::onLoadHistorySeriesError
        )
    }

    private fun onLoadHistorySeriesSuccess(series: List<Series>) {
        updateState {
            it.copy(
                series = series.map { series -> series.toUiState() },
                screenStatus = ViewAllHistoryScreenState.SectionStatus.SUCCESS
            )
        }
        updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.SUCCESS)
    }

    private fun onLoadHistorySeriesError(throwable: Throwable) {
        updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.ERROR)
        updateErrorStatus(throwable)
    }

    override fun onBackClicked() {
        sendEffect(ViewAllHistoryEffect.OnNavigateBack)
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(ViewAllHistoryEffect.OnMovieClicked(movieId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(ViewAllHistoryEffect.OnSeriesClicked(seriesId))
    }

    override fun onMovieDelete(movieId: Long) {
        tryToCall(
            onStart = { onMovieDeleteStart(movieId) },
            block = { accountUseCase.removeMovieFromHistory(movieId) },
            onSuccess = { onMovieDeleteSuccess() },
            onError = ::onMovieDeleteError
        )
    }

    private fun onMovieDeleteStart(movieId: Long) {
        updateState { state ->
            state.copy(
                movies = state.movies.filter { it.id != movieId },
                deletedMoviesIds = state.deletedMoviesIds + movieId,
                deletedItems = state.deletedItems + "${movieId}, movie"
            )
        }
    }

    private fun onMovieDeleteSuccess() {
        showSnackBar(R.string.movie_history_remove_success, true)
    }

    private fun onMovieDeleteError(throwable: Throwable) {
        showSnackBar(R.string.movie_history_remove_fail, false)
    }

    override fun onSeriesDelete(seriesId: Long) {
        tryToCall(
            onStart = { onSeriesDeleteStart(seriesId) },
            block = { accountUseCase.removeSeriesFromHistory(seriesId) },
            onSuccess = { onSeriesDeleteSuccess() },
            onError = ::onSeriesDeleteEnd
        )
    }

    private fun onSeriesDeleteStart(seriesId: Long) {
        updateState { state ->
            state.copy(
                series = state.series.filter { it.id != seriesId },
                deletedSeriesIds = state.deletedSeriesIds + seriesId,
                deletedItems = state.deletedItems + "${seriesId}, tv"
            )
        }
    }

    private fun onSeriesDeleteSuccess() {
        showSnackBar(R.string.series_history_remove_success, true)
    }

    private fun onSeriesDeleteEnd(throwable: Throwable) {
        showSnackBar(R.string.series_history_remove_fail, false)
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            loadHistory()
            delay(500L)
            updateState { it.copy(isRefreshing = true) }
        }
    }

    override fun onUndoClicked() {
        tryToCall(
            block = ::onUndoClickedBlock,
            onSuccess = { onUndoClickedSuccess() },
            onError = ::onUndoClickedError
        )
    }

    private suspend fun onUndoClickedBlock() {
        val item = screenState.value.deletedItems.last().split(", ")
        when (item[1]) {
            "movie" -> accountUseCase.addMovieToHistory(item[0].toLong())
            "tv" -> accountUseCase.addSeriesToHistory(item[0].toLong())
        }
    }

    private fun onUndoClickedSuccess() {
        val item = screenState.value.deletedItems.last().split(", ")
        when (item[1]) {
            "movie" -> onUndoMovieClicked()
            "tv" -> onUndoSeriesClicked()
        }
    }

    private fun onUndoMovieClicked() {
        updateState { state ->
            state.copy(
                deletedItems = state.deletedItems.dropLast(1),
                deletedMoviesIds = state.deletedMoviesIds.dropLast(1),
                showSnackBar = false
            )
        }
        loadHistoryMovies()
    }

    private fun onUndoSeriesClicked() {
        updateState { state ->
            state.copy(
                deletedItems = state.deletedItems.dropLast(1),
                deletedSeriesIds = state.deletedSeriesIds.dropLast(1),
                showSnackBar = false
            )
        }
        loadHistorySeries()
    }

    private fun onUndoClickedError(throwable: Throwable) {
        showSnackBar(R.string.movie_history_restore_fail, false)
    }

    fun updateScreenStatus(status: ViewAllHistoryScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ViewAllHistoryScreenState.SectionStatus.ERROR) }
    }

    private fun handleError(
        throwable: Throwable,
        updateSection: ViewAllHistoryScreenState.() -> ViewAllHistoryScreenState
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

    fun showSnackBar(messageId: Int, isSuccessful: Boolean) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    snackBarMessageId = messageId,
                    isProcessSuccess = isSuccessful,
                    showSnackBar = true
                )
            }
            delay(2000)
            updateState { it.copy(showSnackBar = false) }
        }
    }
}