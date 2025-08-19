package com.cairosquad.viewmodel.library.view_all_favorite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
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
            block = {
                accountUseCase.getFavoriteSeries(1)
            },
            onSuccess = { seriesList ->
                val filteredSeries = seriesList.filterNot { series ->
                    series.id in screenState.value.deletedSeriesIds
                }.map { it.toUiState() }
                updateState {
                    it.copy(
                        series = filteredSeries,
                        screenStatus = ViewAllFavoriteScreenState.SectionStatus.SUCCESS
                    )
                }
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
                accountUseCase.getFavoriteMovies(1)
            },
            onSuccess = { movies ->
                val filteredMovies = movies.filterNot { movie ->
                    movie.id in screenState.value.deletedMoviesIds
                }.map { it.toUiState() }
                updateState {
                    it.copy(
                        movies = filteredMovies,
                        screenStatus = ViewAllFavoriteScreenState.SectionStatus.SUCCESS
                    )
                }
            },
            onError = {
                updateScreenStatus(ViewAllFavoriteScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    override fun onBackClick() {
        sendEffect(ViewAllFavoriteEffect.OnNavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(ViewAllFavoriteEffect.OnMovieClicked(movieId))
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(ViewAllFavoriteEffect.OnSeriesClicked(seriesId))
    }

    override fun onMovieDelete(movieId: Long) {
        tryToCall(
            onStart = {
                updateState { state ->
                    state.copy(
                        movies = state.movies.filter { it.id != movieId },
                        deletedMoviesIds = state.deletedMoviesIds + movieId,
                        deletedItems = state.deletedItems + "${movieId}, movie"
                    )
                }
            },
            block = { accountUseCase.removeMovieFromFavorite(movieId) },
            onSuccess = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        isProcessSuccess = true,
                        snackMessageId = R.string.movie_favorite_remove_success
                    )
                }
            },
            onError = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        isProcessSuccess = false,
                        snackMessageId = R.string.movie_favorite_remove_fail
                    )
                }
            },
            onEnd = {
                delay(2000)
                updateState {
                    it.copy(showSnackBar = false)
                }
            }
        )
    }

    override fun onSeriesDelete(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState { state ->
                    state.copy(
                        series = state.series.filter { it.id != seriesId },
                        deletedSeriesIds = state.deletedSeriesIds + seriesId,
                        deletedItems = state.deletedItems + "${seriesId}, series"
                    )
                }
            },
            block = { accountUseCase.removeSeriesFromFavorite(seriesId) },
            onSuccess = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        isProcessSuccess = true,
                        snackMessageId = R.string.series_favorite_remove_success
                    )
                }
            },
            onError = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        isProcessSuccess = false,
                        snackMessageId = R.string.series_favorite_remove_fail
                    )
                }
            },
            onEnd = {
                delay(2000)
                updateState {
                    it.copy(showSnackBar = false)
                }
            }
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

    override fun onUndoClick() {
        tryToCall(
            block = {
                val item = screenState.value.deletedItems.last().split(", ")
                when (item[1]) {
                    "movie" -> {
                        accountUseCase.addMovieToFavorite(item[0].toLong())
                    }

                    "series" -> {
                        accountUseCase.addSeriesToFavorite(item[0].toLong())
                    }
                }
            },
            onSuccess = {
                val item = screenState.value.deletedItems.last().split(", ")

                when (item[1]) {
                    "movie" -> {
                        updateState {
                            it.copy(
                                deletedItems = it.deletedItems.dropLast(1),
                                deletedMoviesIds = it.deletedMoviesIds.dropLast(1),
                                showSnackBar = false
                            )
                        }
                        getFavoriteMovies()
                    }

                    "series" -> {
                        updateState {
                            it.copy(
                                deletedItems = it.deletedItems.dropLast(1),
                                deletedSeriesIds = it.deletedSeriesIds.dropLast(1),
                                showSnackBar = false
                            )
                        }
                        getFavoriteSeries()
                    }
                }
            },
            onError = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        isProcessSuccess = false,
                        snackMessageId = R.string.favorite_restore_fail
                    )
                }
            },
            onEnd = {
                delay(2000)
                updateState {
                    it.copy(showSnackBar = false)
                }
            }
        )
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
}