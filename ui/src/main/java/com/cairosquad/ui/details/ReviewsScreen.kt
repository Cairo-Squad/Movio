package com.cairosquad.ui.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoadingReviewCard
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.reviews.ReviewsEffect
import com.cairosquad.viewmodel.details.reviews.ReviewsInteractionListener
import com.cairosquad.viewmodel.details.reviews.ReviewsScreenState
import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReviewsScreen(
    mediaId: Long,
    isMovie: Boolean,
    viewModel: ReviewsViewModel = koinViewModel(
        parameters = { parametersOf(mediaId, isMovie) }
    )
) {
    val navController = LocalNavController.current
    val state = viewModel.screenState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.getReviews()
    }

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ReviewsEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
            }

            is ReviewsEffect.NavigateBack -> navController.popBackStack()

        }

    }

    ReviewsContent(
        listener = viewModel,
        state = state.value,
    )
}

@Composable
private fun ReviewsContent(
    listener: ReviewsInteractionListener,
    state: ReviewsScreenState
) {
    when {
        state.isLoading -> {
            ReviewsLoadingContent()
        }

        else -> Box {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .size(230.dp)
                    .blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(
                        color = Theme.color.surfaces.onSurfaceAt5,
                        shape = CircleShape
                    )
                    .align(Alignment.TopEnd)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()

                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppBar(
                    title = stringResource(R.string.reviews),
                    onBackButtonClicked = listener::onClickBack,
                )
                LazyColumn(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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

    }
}

@Composable
private fun ReviewsLoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(12) {
                LoadingReviewCard()
            }
        }
    }
}
