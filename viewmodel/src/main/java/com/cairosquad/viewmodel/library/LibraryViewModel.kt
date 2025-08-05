package com.cairosquad.viewmodel.library

import android.util.Log
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
) : BaseViewModel<LibraryScreenState, LibraryEffect>(LibraryScreenState()),
    LibraryInteractionListener {

    init {
        loadScreenState()
    }

    fun loadScreenState() {
        loadMoviesLists()
        loadSeriesLists()
        loadHistoryMovies()
        loadHistorySeries()
    }

    private fun loadMoviesLists() {
        tryToCall(
            block = accountUseCase::getMoviesLists,
            onSuccess = ::onLoadingMoviesListsSuccess,
            onError = {
                updateState { it.copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
            },
            onEnd = {}
        )
    }

    private fun onLoadingMoviesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
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
                updateState { it.copy(listsSectionState = LibraryScreenState.SectionStatus.ERROR) }
            },
        )
    }

    private fun onLoadingSeriesListsSuccess(mediaLists: List<MediaList>) {
        updateState {
            it.copy(
                seriesLists = mediaLists.map { mediaList -> mediaList.toUiState() },
                listsSectionState = LibraryScreenState.SectionStatus.SUCCESS
            )
        }
    }

    private fun loadHistoryMovies() {
        tryToCall(
            block = { accountUseCase.getHistoryMovies(1) },
            onSuccess = ::onLoadingHistoryMoviesSuccess,
            onError = {
                updateState { it.copy(historySectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingHistoryMoviesSuccess(movies: List<Movie>) {
        Log.d("history remote", "onLoadingHistoryMoviesSuccess viewModel: $movies")
        updateState {
            it.copy(
                historyMovies = movies.map { movie -> movie.toUiState() },
                historySectionState = LibraryScreenState.SectionStatus.SUCCESS
            )
        }
    }

    private fun loadHistorySeries() {
        tryToCall(
            block = { accountUseCase.getHistorySeries(1) },
            onSuccess = ::onLoadingHistorySeriesSuccess,
            onError = {
                updateState { it.copy(historySectionState = LibraryScreenState.SectionStatus.ERROR) }
            }
        )
    }

    private fun onLoadingHistorySeriesSuccess(series: List<Series>) {
        updateState {
            it.copy(
                historySeries = series.map { series -> series.toUiState() },
                historySectionState = LibraryScreenState.SectionStatus.SUCCESS
            )
        }
    }
}