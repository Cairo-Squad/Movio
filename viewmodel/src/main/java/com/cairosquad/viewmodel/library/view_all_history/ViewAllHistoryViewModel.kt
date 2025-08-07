package com.cairosquad.viewmodel.library.view_all_history

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
class ViewAllHistoryViewModel @Inject constructor(
    private val viewAllHistoryPager: ViewAllHistoryPager,
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
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { viewAllHistoryPager.movies() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = {
                updateState {
                    it.copy(movies = it.movies)
                }
                updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
    }

    private fun loadHistorySeries() {
        tryToCall(
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { viewAllHistoryPager.series() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = {
                updateState {
                    it.copy(series = it.series)
                }
                updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.SUCCESS)
            },
            onError = {
                updateScreenStatus(ViewAllHistoryScreenState.SectionStatus.ERROR)
                updateErrorStatus(it)
            }
        )
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

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            loadHistory()
            delay(500L)
            updateState { it.copy(isRefreshing = true) }

        }
    }


    fun updateScreenStatus(status: ViewAllHistoryScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ViewAllHistoryScreenState.SectionStatus.ERROR) }
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
}