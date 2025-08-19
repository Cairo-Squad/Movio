package com.cairosquad.viewmodel.library.view_all_lists

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
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
            block = {
                accountUseCase.getMoviesLists(1)
            },
            onSuccess = { movieLists ->
                updateState {
                    it.copy(movieLists = movieLists.map { it.toUiState() })
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
                accountUseCase.getSeriesLists(1)
            },
            onSuccess = { seriesLists ->
                updateState {
                    it.copy(seriesLists = seriesLists.map { it.toUiState() })
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
            delay(500L)
            updateState { it.copy(isRefreshing = true) }
        }
    }

    override fun onAddListClick() {
        updateState { it.copy(
            showCreateListBottomSheet = true,
            listName = ""
        ) }
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
                Pair(accountUseCase.getMoviesLists(1), accountUseCase.getSeriesLists(1))
            },
            onSuccess = { (moviesLists, seriesLists) ->
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
                delay(2000L)
                updateState { it.copy(showSnackBar = false) }
            },
            onError = {
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
                delay(2000L)
                updateState { it.copy(showSnackBar = false) }
            }
        )
    }

    fun updateScreenStatus(status: ViewAllListsScreenState.SectionStatus) {
        updateState { it.copy(screenStatus = status) }
    }

    fun updateErrorStatus(throwable: Throwable) {
        handleError(throwable) { copy(screenStatus = ViewAllListsScreenState.SectionStatus.ERROR) }
    }

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