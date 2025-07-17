package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.domain.search.usecase.GetMoviesDetailsUseCase
import com.cairosquad.domain.search.usecase.GetSeriesDetailsUseCase
import com.cairosquad.viewmodel.base.BaseViewModel

class ReviewsViewModel(
    private val mediaId: Long,
    private val isMovie: Boolean,
    private val getMoviesDetailsUseCase: GetMoviesDetailsUseCase,
    private val getSeriesDetailsUseCase: GetSeriesDetailsUseCase,
) : BaseViewModel<ReviewsScreenState, Nothing>(ReviewsScreenState()) {
    init {
        getReviews()
    }

    private fun getReviews() {
        updateState { it.copy(isLoading = true, error = null) }
        tryToCall(
            block = {
                val reviews = if (isMovie) {
                    getMoviesDetailsUseCase.getMovieReviews(mediaId)
                } else {
                    getSeriesDetailsUseCase.getSeriesReviews(mediaId)
                }
                reviews
            },
            onSuccess = { reviews ->
                updateState {
                    it.copy(
                        isLoading = false,
                        reviews = reviews.map { it.toUiState() }
                    )
                }
            },
            onError = { error ->
                updateState {
                    it.copy(isLoading = false, error = error.message)
                }
            }
        )
    }
}