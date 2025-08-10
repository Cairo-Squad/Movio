package com.cairosquad.viewmodel.library.list_content

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    }

    private fun loadMoviesOfList(listId: Long) {
        tryToCall(
            block = {
                accountUseCase.getMoviesOfList(listId, 1)
            },
            onSuccess = { movies ->
                val filteredMovies = movies.filter { movie ->
                    movie.id !in screenState.value.deletedMoviesIds
                }
                updateState {
                    it.copy(
                        movies = filteredMovies.map { it.toUiState() },
                        screenStatus = ListContentScreenState.SectionStatus.SUCCESS
                    )
                }
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
            onStart = {
                updateState { state ->
                    state.copy(
                        movies = state.movies.filter { it.id != movieId },
                        deletedMoviesIds = state.deletedMoviesIds + movieId,
                        deletedItems = state.deletedItems + "${movieId}, movie"
                    )
                }
            },
            block = {
                accountUseCase.removeMovieFromList(listId, movieId)
            },
            onSuccess = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessageId = R.string.movie_list_remove_success
                    )
                }
            },
            onError = {
                updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessageId = R.string.movie_list_remove_fail
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

    override fun onUndoClicked() {
        tryToCall(
            block = {
                val item = screenState.value.deletedItems.last().split(", ")
                when (item[1]) {
                    "movie" -> {
                        accountUseCase.addMovieToList(listId, item[0].toLong())
                    }

                    "series" -> {
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
                        getListDetails()
                    }
                }
            },
            onError = {
                updateState {
                    it.copy(
                        showSnackBar = true,
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

    fun updateScreenStatus(status: ListContentScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ListContentScreenState.SectionStatus.ERROR) }
    }

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