package com.cairosquad.viewmodel.library.list_content

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
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
            block = { accountUseCase.getMoviesOfList(listId, FIRST_PAGE) },
            onSuccess = ::onGetMoviesOfListSuccess,
            onError = ::onGetMoviesOfListError
        )
    }

    private fun onGetMoviesOfListSuccess(movies: List<Movie>) {
        val filteredMovies = movies.filter { movie ->
            movie.id !in screenState.value.deletedMoviesIds
        }
        updateState {
            it.copy(
                movies = filteredMovies.map { it.toUiState() },
                screenStatus = ListContentScreenState.SectionStatus.SUCCESS
            )
        }
    }

    private fun onGetMoviesOfListError(throwable: Throwable) {
        updateScreenStatus(ListContentScreenState.SectionStatus.ERROR)
        updateErrorStatus(throwable)
    }

    override fun onBackClick() {
        sendEffect(ListContentEffect.OnNavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(ListContentEffect.OnMovieClicked(movieId))
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(ListContentEffect.OnSeriesClicked(seriesId))
    }

    override fun onMovieDelete(movieId: Long) {
        tryToCall(
            onStart = { onStartDeleteMovie(movieId) },
            block = { accountUseCase.removeMovieFromList(listId, movieId) },
            onSuccess = { onMovieDeleteSuccess() },
            onError = { onMovieDeleteError() },
            onEnd = { onDeleteEnd() }
        )
    }

    private fun onStartDeleteMovie(movieId: Long) {
        updateState { state ->
            state.copy(
                movies = state.movies.filter { it.id != movieId },
                deletedMoviesIds = state.deletedMoviesIds + movieId,
                deletedItems = state.deletedItems + "$movieId, TYPE_MOVIE"
            )
        }
    }

    private fun onMovieDeleteSuccess() {
        updateState {
            it.copy(
                showSnackBar = true,
                snackMessageId = R.string.movie_list_remove_success
            )
        }
    }

    private fun onMovieDeleteError() {
        updateState {
            it.copy(
                showSnackBar = true,
                snackMessageId = R.string.movie_list_remove_fail
            )
        }
    }

    private suspend fun onDeleteEnd() {
        delay(SNACKBAR_DURATION)
        updateState { it.copy(showSnackBar = false) }
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
            delay(REFRESH_DELAY)
            updateState { it.copy(isRefreshing = true) }
        }
    }

    override fun onUndoClick() {
        tryToCall(
            block = {
                val item = screenState.value.deletedItems.last().split(ITEM_DELIMITER)
                when (item[FIRST_PAGE]) {
                    TYPE_MOVIE -> accountUseCase.addMovieToList(listId, item[0].toLong())
                    TYPE_SERIES -> {}
                }
            },
            onSuccess = { onUndoSuccess() },
            onError = { onUndoError() },
            onEnd = { onUndoEnd() }
        )
    }

    private fun onUndoSuccess() {
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
                getListDetails()
            }
        }
    }

    private fun onUndoError() {
        updateState {
            it.copy(
                showSnackBar = true,
                snackMessageId = R.string.favorite_restore_fail
            )
        }
    }

    private suspend fun onUndoEnd() {
        delay(SNACKBAR_DURATION)
        updateState { it.copy(showSnackBar = false) }
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

    companion object {
        private const val FIRST_PAGE = 1
        private const val REFRESH_DELAY = 500L
        private const val SNACKBAR_DURATION = 2000L

        private const val ITEM_DELIMITER = ", "
        private const val TYPE_MOVIE = "movie"
        private const val TYPE_SERIES = "series"
    }
}