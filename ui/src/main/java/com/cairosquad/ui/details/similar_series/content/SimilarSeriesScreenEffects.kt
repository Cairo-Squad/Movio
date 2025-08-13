package com.cairosquad.ui.details.similar_series.content

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesEffect
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesViewModel

@Composable
fun SimilarSeriesScreenEffects(
    viewModel: SimilarSeriesViewModel,
    navController: NavController
) {
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            is SimilarSeriesEffect.NavigateBack -> navController.popBackStack()

            is SimilarSeriesEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }
        }
    }
}