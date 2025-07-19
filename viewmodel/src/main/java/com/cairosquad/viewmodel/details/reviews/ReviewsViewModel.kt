package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.domain.usecase.movies.GetMovieDetailsUseCase
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ReviewsViewModel(
    private val mediaId: Long,
    private val isMovie: Boolean,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSeriesDetailsUseCase: GetSeriesDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

) : BaseViewModel<ReviewsScreenState, ReviewsEffect>(initialState = ReviewsScreenState()),
    ReviewsInteractionListener {

    fun getReviews() {
        updateState { it.copy(isLoading = true, error = null) }
        tryToCall(
            block = { getReviewsByType() },
            onSuccess = ::handleSuccess,

            onError = ::handleError,

            dispatcher = dispatcher
        )
    }

    private suspend fun getReviewsByType(): List<Review> {
        return if (isMovie) {
            getMovieDetailsUseCase.getMovieReviews(mediaId)
        } else {
            getSeriesDetailsUseCase.getSeriesReviews(mediaId, 1)
        }
    }

    private fun handleError(error: Throwable) {
        updateState {
            it.copy(isLoading = false, error = error.message)
        }
    }

    private fun handleSuccess(reviews: List<Review>) {
        updateState {
            it.copy(
                isLoading = false,
                reviews = reviews.map { it.toUiState() }
            )
        }
    }

    override fun onClickBack() {
        sendEffect(ReviewsEffect.NavigateBack)
    }
}