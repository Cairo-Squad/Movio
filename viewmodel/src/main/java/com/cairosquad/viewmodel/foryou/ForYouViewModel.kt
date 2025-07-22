package com.cairosquad.viewmodel.foryou

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.foryou.ForYouState.ScreenStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ForYouViewModel(private val forYouPager: ForYouPager) :
    BaseViewModel<ForYouState, ForYouEffect>(ForYouState()), ForYouInteractionListener {

    init {
        getForYouMovies()
    }

    private fun getForYouMovies() {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.LOADING,
                errorStatus = null,
                isEmpty = false
            )
        }
        tryToCall(
            block = {
                val forYouMovies = cacheMappedPagingData(
                    scope = viewModelScope,
                    fetch = { forYouPager.movies() },
                    map = { it.toUiState() }
                )
                forYouMovies
            },
            onSuccess = { forYouMovies ->
                updateState {
                    it.copy(
                        forYou = forYouMovies,
                        screenStatus = ScreenStatus.SUCCESS,
                        //isRefreshing = false
                    )
                }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.FAILED,
                        errorStatus = handleSearchException(e)
                    )
                }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun <T : Any, R : Any> cacheMappedPagingData(
        scope: CoroutineScope,
        fetch: () -> Flow<PagingData<T>>,
        map: (T) -> R
    ): Flow<PagingData<R>> = fetch()
        .map { pagingData -> pagingData.map(map) }
        .cachedIn(scope)

    fun handleSearchException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            getForYouMovies()
            delay(500L)
            updateState { it.copy(isRefreshing = false) }
        }
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(ForYouEffect.NavigateToMovieDetails(movieId))
    }
}