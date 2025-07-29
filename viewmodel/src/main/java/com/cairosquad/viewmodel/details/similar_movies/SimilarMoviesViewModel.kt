package com.cairosquad.viewmodel.details.similar_movies

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class SimilarMoviesViewModel(
    private val manageMoviesUseCase: ManageMoviesUseCase
) : BaseViewModel<SimilarMoviesScreenState, SimilarMoviesEffect>(SimilarMoviesScreenState()),
    SimilarMoviesInteractionListener {


    fun fetchSimilarMovies(movieId: Long) {
        tryToCall(
            onStart = {
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.LOADING
                    )
                }
            },
            block = {
                manageMoviesUseCase.getSimilarMovies(movieId)
            }, onSuccess = { movies ->
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.SUCCESS,
                        movies = movies.map { it.toUiState() }
                    )
                }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.ERROR,
                        errorStatus = when(e){
                            is MovioException ->
                                exceptionToErrorStatus(e)
                            else -> ErrorStatus.UNKNOWN_ERROR

                        }
                    )
                }

            },
            dispatcher = Dispatchers.IO


        )

    }


    override fun onClickBack() {
        sendEffect(SimilarMoviesEffect.NavigateBack)
    }

    override fun onMovieClicked(movieId: Long) {
        sendEffect(SimilarMoviesEffect.NavigateToMovieDetails(movieId))
    }


}