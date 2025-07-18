package com.cairosquad.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.viewmodel.details.reviews.ReviewsScreenState.ReviewUiState
import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReviewsScreen(
    mediaId: Long,
    isMovie: Boolean,
    navController: NavHostController,
    viewModel: ReviewsViewModel = koinViewModel<ReviewsViewModel>(
        parameters = { parametersOf(mediaId, isMovie) }
    )
) {
    val state = viewModel.screenState.collectAsState()

    ReviewsContent(
        onBackClicked = { navController.popBackStack() },
        reviews = state.value.reviews,
    )
}

@Composable
private fun ReviewsContent(
    onBackClicked: () -> Unit,
    reviews: List<ReviewUiState>
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AppBar(
            title = stringResource(R.string.reviews),
            onBackButtonClicked = onBackClicked,
        )
        LazyColumn(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { review ->
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
