package com.cairosquad.ui.details.reviews.content

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.BlurredCircle
import com.cairosquad.ui.details.reviews.composable.Reviews
import com.cairosquad.viewmodel.details.reviews.ReviewsInteractionListener
import com.cairosquad.viewmodel.details.reviews.ReviewsScreenState

@Composable
fun ReviewsContent(
    listener: ReviewsInteractionListener,
    state: ReviewsScreenState
) {
    when {
        state.isLoading -> {
            ReviewsLoadingContent()
        }

        state.error != null -> {
            DetailsFailContent(onTryAgainClick = listener::onRefresh)
        }

        else -> Box {
            BlurredCircle()
            Reviews(listener, state)
        }
    }
}
