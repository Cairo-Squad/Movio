package com.cairosquad.viewmodel.library.view_all_lists

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.library.toUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewAllListsViewModel @Inject constructor(
    private val viewAllListsPager: ViewAllListsPager,
) : BaseViewModel<ViewAllListsScreenState, ViewAllListsEffect>(ViewAllListsScreenState()),
    ViewAllListsInteractionListener {

    init {
        loadLists()
    }

    private fun loadLists() {
        getMoviesLists()
        getSeriesLists()
    }

    private fun getMoviesLists() {
        tryToCall(
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { viewAllListsPager.movies() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = { movieLists ->
                updateState {
                    it.copy(movieLists = movieLists)
                }
                updateScreenStatus(ViewAllListsScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ViewAllListsScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    private fun getSeriesLists() {
        tryToCall(
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { viewAllListsPager.series() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = { seriesLists ->
                updateState {
                    it.copy(seriesLists = seriesLists)
                }
                updateScreenStatus(ViewAllListsScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ViewAllListsScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    override fun onNavigateBack() {
        sendEffect(ViewAllListsEffect.OnNavigateBack)
    }

    override fun onSeriesListClicked(listId: Long, listName: String) {
        sendEffect(ViewAllListsEffect.OnSeriesListClicked(listId, listName))
    }

    override fun onMovieListClicked(listId: Long, listName: String) {
        sendEffect(ViewAllListsEffect.OnMovieListClicked(listId, listName))
    }

    override fun onCreateNewListClicked() {
        updateState { it.copy(isCreateListBottomSheetVisible = true) }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            loadLists()
            delay(500L)
            updateState { it.copy(isRefreshing = true) }
        }
    }


    fun updateScreenStatus(status: ViewAllListsScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ViewAllListsScreenState.SectionStatus.ERROR) }
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
        updateSection: ViewAllListsScreenState.() -> ViewAllListsScreenState
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