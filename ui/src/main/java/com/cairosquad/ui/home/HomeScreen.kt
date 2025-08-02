package com.cairosquad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cairosquad.ui.home.content.HomeScreenContent
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeeAllScreenRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.home.HomeEffect
import com.cairosquad.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect) { effect -> effectHandler(effect, navController) }

    val screenState by viewModel.screenState.collectAsState()

    HomeScreenContent(
        screenState = screenState,
        listener = viewModel,
    )

}


private fun effectHandler(
    effect: HomeEffect,
    navController: NavController
) {
    when (effect) {
        is HomeEffect.NavigateMediaDetails -> {
            if (effect.isMovie) {
                navController.navigate(MovieRoute(effect.mediaId))
            } else {
                navController.navigate(SeriesRoute(effect.mediaId))
            }
        }

        HomeEffect.NavigateToProfile -> {
            // TODO
        }

        is HomeEffect.NavigateToSeeAllScreen -> {
            navController.navigate(
                SeeAllScreenRoute(
                    effect.mediaContentType,
                    effect.mediaType
                )
            )
        }
    }
}


