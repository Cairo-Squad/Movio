package com.cairosquad.ui.rated

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.rated.composable.UndoRatingSnackBar
import com.cairosquad.ui.rated.content.MyRatingsScreenContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.rated.MyRatingsEffect
import com.cairosquad.viewmodel.rated.MyRatingsViewModel

@Composable
fun MyRatingsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyRatingsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            MyRatingsEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MyRatingsEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }

            is MyRatingsEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }
        }
    }
    Box {
        MyRatingsScreenContent(
            modifier = modifier,
            state = state,
            listener = viewModel
        )

        UndoRatingSnackBar(state, viewModel)
    }
}