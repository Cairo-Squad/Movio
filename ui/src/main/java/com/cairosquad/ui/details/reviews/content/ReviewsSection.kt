package com.cairosquad.ui.details.reviews.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.viewmodel.details.reviews.ReviewsInteractionListener
import com.cairosquad.viewmodel.details.reviews.ReviewsScreenState


@Composable
fun Reviews(
    listener: ReviewsInteractionListener,
    state: ReviewsScreenState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            title = stringResource(R.string.reviews),
            onBackButtonClicked = listener::onBackClick,
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
        ) {
            items(state.reviews) { review ->
                ReviewCard(
                    modifier = Modifier.fillMaxWidth(),
                    imgUrl = review.reviewerImageUrl,
                    rating = review.rating,
                    reviewDate = review.reviewDate,
                    reviewText = review.reviewText,
                    reviewerName = review.reviewerName
                )
            }
        }
    }
}