package com.cairosquad.viewmodel.library.view_all_favorite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
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
    private val viewAllFavoritePager: ViewAllFavoritePager,
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
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { viewAllFavoritePager.series() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = {
                val filteredSeries = screenState.value.series.map { pagingData ->
                    pagingData.filter { movie -> movie.id !in screenState.value.deletedSeriesIds }
                }
                updateState {
                    it.copy(series = filteredSeries)
                }
                updateScreenStatus(ViewAllFavoriteScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ViewAllFavoriteScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    private fun getFavoriteMovies() {
        tryToCall(
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { viewAllFavoritePager.movies() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = {
                val filteredMovies = screenState.value.movies.map { pagingData ->
                    pagingData.filter { movie -> movie.id !in screenState.value.deletedMoviesIds }
                }
                updateState {
                    it.copy(movies = filteredMovies)
                }
                updateScreenStatus(ViewAllFavoriteScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ViewAllFavoriteScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    override fun onBackClicked() {
        sendEffect(ViewAllFavoriteEffect.OnNavigateBack)
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(ViewAllFavoriteEffect.OnMovieClicked(movieId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(ViewAllFavoriteEffect.OnSeriesClicked(seriesId))
    }

    override fun onMovieDelete(movieId: Long) {
        tryToCall(
            onStart = { updateState { it.copy(deletedMoviesIds = it.deletedMoviesIds + movieId) } },
            block = {},
            onSuccess = {},
            onError = {}
        )
    }

    override fun onSeriesDelete(seriesId: Long) {
        tryToCall(
            onStart = { updateState { it.copy(deletedSeriesIds = it.deletedSeriesIds + seriesId) } },
            block = {},
            onSuccess = {},
            onError = {}
        )
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            getFavorites()
            delay(500L)
            updateState { it.copy(isRefreshing = true) }
        }
    }

    fun updateScreenStatus(status: ViewAllFavoriteScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
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
}