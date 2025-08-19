package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel(assistedFactory = ReviewsViewModel.Factory::class)
class ReviewsViewModel @AssistedInject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    @Assisted private val mediaId: Long,
    @Assisted private val isMovie: Boolean = false,
    @Assisted private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<ReviewsScreenState, ReviewsEffect>(initialState = ReviewsScreenState()),
    ReviewsInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(
            mediaId: Long,
            isMovie: Boolean,
            dispatcher: CoroutineDispatcher,
        ): ReviewsViewModel
    }

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

    override fun onBackClick() {
        sendEffect(ReviewsEffect.NavigateBack)
    }

    override fun onRefresh() {
            getReviews()

    }
}