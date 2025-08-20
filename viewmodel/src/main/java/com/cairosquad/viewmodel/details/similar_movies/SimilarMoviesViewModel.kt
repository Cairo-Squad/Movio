package com.cairosquad.viewmodel.details.similar_movies

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SimilarMoviesViewModel @Inject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase
) : BaseViewModel<SimilarMoviesScreenState, SimilarMoviesEffect>(SimilarMoviesScreenState()),
    SimilarMoviesInteractionListener {

    fun fetchSimilarMovies(movieId: Long) {
        tryToCall(
            onStart = ::onFetchStart,
            block = { manageMoviesUseCase.getSimilarMovies(movieId) },
            onSuccess = ::onFetchSuccess,
            onError = ::onFetchError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onFetchStart() {
        updateState { it.copy(screenStatus = ScreenStatus.LOADING) }
    }

    private fun onFetchSuccess(movies: List<Movie>) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.SUCCESS,
                movies = movies.map { it.toUiState() }
            )
        }
    }

    private fun onFetchError(e: Throwable) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.ERROR,
                errorStatus = when (e) {
                    is MovioException -> exceptionToErrorStatus(e)
                    else -> ErrorStatus.UNKNOWN_ERROR
                }
            )
        }
    }

    override fun onBackClick() {
        sendEffect(SimilarMoviesEffect.NavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(SimilarMoviesEffect.NavigateToMovieDetails(movieId))
    }

    override fun onRefresh(movieId: Long) {
        updateState { it.copy(isRefreshing = true) }
        fetchSimilarMovies(movieId)
        updateState { it.copy(isRefreshing = false) }
    }
}