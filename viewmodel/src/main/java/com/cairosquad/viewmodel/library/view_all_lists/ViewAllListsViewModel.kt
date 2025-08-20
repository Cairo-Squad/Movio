package com.cairosquad.viewmodel.library.view_all_lists

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.library.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewAllListsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
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
            onStart = ::onFetchStart,
            block = { accountUseCase.getMoviesLists(FIRST_PAGE) },
            onSuccess = ::onMoviesSuccess,
            onError = ::onFetchError
        )
    }

    private fun getSeriesLists() {
        tryToCall(
            onStart = ::onFetchStart,
            block = { accountUseCase.getSeriesLists(FIRST_PAGE) },
            onSuccess = ::onSeriesSuccess,
            onError = ::onFetchError
        )
    }

    private fun onFetchStart() {
        updateScreenStatus(ViewAllListsScreenState.SectionStatus.LOADING)
        updateErrorStatus(null)
    }

    private fun onMoviesSuccess(movieLists: List<MediaList>) {
        updateState {
            it.copy(movieLists = movieLists.map { list -> list.toUiState() })
        }
        updateScreenStatus(ViewAllListsScreenState.SectionStatus.SUCCESS)
    }

    private fun onSeriesSuccess(seriesLists: List<MediaList>) {
        updateState {
            it.copy(seriesLists = seriesLists.map { list -> list.toUiState() })
        }
        updateScreenStatus(ViewAllListsScreenState.SectionStatus.SUCCESS)
    }

    private fun onFetchError(e: Throwable) {
        updateScreenStatus(ViewAllListsScreenState.SectionStatus.ERROR)
        updateErrorStatus(e)
    }

    override fun onNavigateBack() {
        sendEffect(ViewAllListsEffect.OnNavigateBack)
    }

    override fun onSeriesListClick(listId: Long, listName: String) {
        sendEffect(ViewAllListsEffect.OnSeriesListClicked(listId, listName))
    }

    override fun onMovieListClick(listId: Long, listName: String) {
        sendEffect(ViewAllListsEffect.OnMovieListClicked(listId, listName))
    }

    override fun onCreateNewListClick() {
        updateState { it.copy(isCreateListBottomSheetVisible = true) }
    }

    override fun onRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            loadLists()
            delay(REFRESH_DELAY)
            updateState { it.copy(isRefreshing = false) }
        }
    }

    override fun onAddListClick() {
        updateState {
            it.copy(
                showCreateListBottomSheet = true,
                listName = ""
            )
        }
    }

    override fun onDismissCreateListBottomSheet() {
        updateState { it.copy(showCreateListBottomSheet = false) }
    }

    override fun onListValueChange(listName: String) {
        updateState { it.copy(listName = listName) }
    }

    override fun onSubmitCreateListClick() {
        if (screenState.value.isCreatingList) return

        updateState { it.copy(isCreatingList = true) }

        tryToCall(
            block = {
                accountUseCase.createList(screenState.value.listName)
                Pair(accountUseCase.getMoviesLists(FIRST_PAGE), accountUseCase.getSeriesLists(FIRST_PAGE))
            },
            onSuccess = { (moviesLists, seriesLists) ->
                onListCreatedSuccess(moviesLists, seriesLists)
            },
            onError = ::onListCreatedError
        )
    }

    private suspend fun onListCreatedSuccess(
        moviesLists: List<MediaList>,
        seriesLists: List<MediaList>
    ) {
        updateState {
            it.copy(
                isCreatingList = false,
                showCreateListBottomSheet = false,
                listName = "",
                movieLists = moviesLists.map { list -> list.toUiState() },
                seriesLists = seriesLists.map { list -> list.toUiState() },
                showSnackBar = true,
                isProcessSuccess = true,
                snackMessageId = R.string.list_created_successfully
            )
        }
        delay(SNACKBAR_DURATION)
        updateState { it.copy(showSnackBar = false) }
    }

    private suspend fun onListCreatedError(e: Throwable) {
        updateState {
            it.copy(
                isCreatingList = false,
                showCreateListBottomSheet = false,
                listName = "",
                showSnackBar = true,
                isProcessSuccess = false,
                snackMessageId = R.string.error_creating_list
            )
        }
        delay(SNACKBAR_DURATION)
        updateState { it.copy(showSnackBar = false) }
    }

    fun updateScreenStatus(status: ViewAllListsScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(errorStatus: ErrorStatus?) {
        updateState { it.copy(errorStatus = errorStatus) }
    }

    private fun updateErrorStatus(throwable: Throwable) {
        updateState {
            it.copy(
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
    }
}