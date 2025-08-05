package com.cairosquad.viewmodel.library

import android.util.Log
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.MediaList
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
) : BaseViewModel<LibraryScreenState, LibraryEffect>(LibraryScreenState()),
    LibraryInteractionListener {

    init {
        loadMoviesLists()
        loadSeriesLists()
    }

    private fun loadMoviesLists() {
        tryToCall(
            block = accountUseCase::getMoviesLists,
            onSuccess = ::onLoadingMoviesListsSuccess,
            onError = {
                Log.d("View Model test", "onLoadingMoviesListsError: $it")
                updateState { it.copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
            },
            onEnd = {}
        )
    }

    private fun onLoadingMoviesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            Log.d("View Model test", "onLoadingMoviesListsSuccess: $mediaLists")
            it.copy(
                movieLists = mediaLists.map { mediaList -> mediaList.toUiState() },
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            )
        }
    }

    private fun loadSeriesLists() {
        tryToCall(
            block = accountUseCase::getSeriesLists,
            onSuccess = ::onLoadingSeriesListsSuccess,
            onError = {
                Log.d("View Model test", "onLoadingMoviesListsError: $it")
                updateState { it.copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
            },
            onEnd = {}
        )
    }

    private fun onLoadingSeriesListsSuccess(mediaLists: List<MediaList>) {
        Log.d("View Model test", "onLoadingMoviesListsSuccess: $mediaLists")
        updateState {
            it.copy(
                seriesLists = mediaLists.map { mediaList -> mediaList.toUiState() },
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            )
        }
    }
}