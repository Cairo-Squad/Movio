package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<ReviewsScreenState, ReviewsEffect>(initialState = ReviewsScreenState()),
    ReviewsInteractionListener {

    private val mediaId: Long = 0 // // TODO: get from savedHandle
    private val isMovie: Boolean = false // TODO: get from savedHandle

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
            manageMoviesUseCase.getMovieReviews(mediaId)
        } else {
            manageSeriesUseCase.getSeriesReviews(mediaId, 1)
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