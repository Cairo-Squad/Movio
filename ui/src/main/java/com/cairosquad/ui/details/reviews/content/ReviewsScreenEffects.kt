package com.cairosquad.ui.details.reviews.content

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.reviews.ReviewsEffect
import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel

@Composable
fun ReviewsScreenEffects(
    viewModel: ReviewsViewModel,
    navController: NavHostController
) {
    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ReviewsEffect.NavigateBack -> navController.popBackStack()
        }
    }
}