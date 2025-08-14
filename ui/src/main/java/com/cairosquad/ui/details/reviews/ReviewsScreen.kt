package com.cairosquad.ui.details.reviews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.ui.R
import com.cairosquad.ui.details.reviews.content.ReviewsContent
import com.cairosquad.ui.details.reviews.content.ReviewsScreenEffects
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.utils.getSnackBarIcon
import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun ReviewsScreen(
    mediaId: Long,
    isMovie: Boolean,
) {
    val viewModel: ReviewsViewModel =
        hiltViewModel<ReviewsViewModel, ReviewsViewModel.Factory> { factory ->
            factory.create(
                mediaId = mediaId,
                isMovie = isMovie,
                dispatcher = Dispatchers.IO
            )
        }
    val navController = LocalNavController.current
    val state = viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getReviews()
    }

    ReviewsScreenEffects(viewModel, navController)

    ReviewsContent(
        listener = viewModel,
        state = state.value,
    )

    if (state.value.error != null) {
        SnackBar(
            imageVector = getSnackBarIcon(false),
            message = state.value.error ?: stringResource(R.string.something_went_wrong),
            action = {}
        )
    }
}