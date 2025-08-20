package com.cairosquad.viewmodel.foryou

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.foryou.ForYouScreenState.MovieUiState
import com.cairosquad.viewmodel.foryou.ForYouScreenState.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val forYouPager: ForYouPager
) : BaseViewModel<ForYouScreenState, ForYouEffect>(ForYouScreenState()), ForYouInteractionListener {

    init {
        getForYouMovies()
    }

    private fun getForYouMovies() {
        tryToCall(
            onStart = ::onFetchStart,
            block = {
                cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { forYouPager.movies() },
                    map = { it.toUiState() }
                )
            },
            onSuccess = ::onFetchSuccess,
            onError = ::onFetchError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onFetchStart() {
        updateScreenStatus(ScreenStatus.LOADING)
        updateErrorStatus(null)
        updateState { it.copy(isEmpty = false) }
    }

    private fun onFetchSuccess(forYouMovies: Flow<PagingData<MovieUiState>>) {
        updateState { it.copy(forYou = forYouMovies) }
        updateScreenStatus(ScreenStatus.SUCCESS)
    }

    private fun onFetchError(e: Throwable) {
        updateScreenStatus(ScreenStatus.FAILED)
        updateErrorStatus(handleSearchException(e))
    }

    private fun <T : Any, R : Any> cacheMappedPagingData(
        scope: CoroutineScope,
        fetch: () -> Flow<PagingData<T>>,
        map: (T) -> R
    ): Flow<PagingData<R>> = fetch()
        .map { pagingData -> pagingData.map(map) }
        .cachedIn(scope)

    private fun handleSearchException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> exceptionToErrorStatus(e)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }

    fun updateScreenStatus(status: ScreenStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(errorStatus: ErrorStatus?) {
        updateState { it.copy(errorStatus = errorStatus) }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            getForYouMovies()
            delay(ON_REFRESH)
            updateState { it.copy(isRefreshing = false) }
        }
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(ForYouEffect.NavigateToMovieDetails(movieId))
    }

     companion object {
         private const val ON_REFRESH = 500L
    }
}