package com.cairosquad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.ui.home.content.HomeFailContent
import com.cairosquad.ui.home.content.HomeScreenContent
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeeAllScreenRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.search.content.SearchFailContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.home.HomeEffect
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {

    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect) { effect -> effectHandler(effect, navController) }

    val screenState by viewModel.screenState.collectAsState()

    RefreshBox(
        isRefreshing = screenState.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    ) {
        if (screenState.screenStatus == HomeScreenState.ScreenStatus.FAILED) {
            HomeFailContent(
                errorStatus = screenState.errorStatus,
                onRetry = { viewModel.onRetry() }
            )
        } else {
            HomeScreenContent(
                screenState = screenState,
                listener = viewModel,
            )
        }
    }
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


