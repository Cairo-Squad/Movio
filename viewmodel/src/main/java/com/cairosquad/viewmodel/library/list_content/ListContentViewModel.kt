package com.cairosquad.viewmodel.library.list_content

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ListContentViewModel.Factory::class)
class ListContentViewModel @AssistedInject constructor(
    private val listContentPager: ListContentPager,
    private val accountUseCase: AccountUseCase,
    @Assisted private val listId: Long
) : BaseViewModel<ListContentScreenState, ListContentEffect>(ListContentScreenState()),
    ListContentInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(seriesId: Long): ListContentViewModel
    }

    init {
        getListDetails()
    }

    private fun getListDetails() {
        loadMoviesOfList(listId)
        loadSeriesOfList(listId)
    }

    private fun loadMoviesOfList(listId: Long) {
        tryToCall(
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { listContentPager.movies(listId) },
                    map = { it.toUiState() }
                )
            },
            onSuccess = { movies ->
                val filteredMovies = screenState.value.movies.map { pagingData ->
                    pagingData.filter { movie -> movie.id !in screenState.value.deletedMoviesIds }
                }
                updateState {
                    it.copy(movies = filteredMovies)
                }
                updateScreenStatus(ListContentScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ListContentScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    private fun loadSeriesOfList(listId: Long) {
        tryToCall(
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { listContentPager.series(listId) },
                    map = { it.toUiState() }
                )
            },
            onSuccess = { series ->
                updateState { it.copy(series = series) }
                updateScreenStatus(ListContentScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ListContentScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    override fun onBackClicked() {
        sendEffect(ListContentEffect.OnNavigateBack)
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(ListContentEffect.OnMovieClicked(movieId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(ListContentEffect.OnSeriesClicked(seriesId))
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
            getListDetails()
            delay(500L)
            updateState { it.copy(isRefreshing = true) }
        }
    }

    fun updateScreenStatus(status: ListContentScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ListContentScreenState.SectionStatus.ERROR) }
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
        updateSection: ListContentScreenState.() -> ListContentScreenState
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