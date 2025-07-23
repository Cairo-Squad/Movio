package com.cairosquad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.cairosquad.ui.home.content.HomeScreenContent
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.home.effect.DiscoverEffect
import com.cairosquad.viewmodel.home.DiscoverViewModel
import com.cairosquad.viewmodel.home.effect.HomeEffect
import com.cairosquad.viewmodel.home.HomeViewModel
import com.cairosquad.viewmodel.home.model.DiscoverType
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    discoverViewModel: DiscoverViewModel = koinViewModel()
) {

    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect) { effect -> effectHandler(effect, navController) }
    ObserveAsEffect(discoverViewModel.effect) { effect ->
        effectDiscoverHandler(
            effect, navController
        )
    }

    val screenState by viewModel.screenState.collectAsState()

    HomeScreenContent(
        screenState = screenState,
        listener = viewModel,
        discoverInteractionsListener = discoverViewModel
    )
}

private fun effectHandler(
    effect: HomeEffect, navController: NavController
) {
    when (effect) {
        is HomeEffect.NavigateMovie -> {
            navController.navigate(MovieRoute(effect.movieId))
        }

        is HomeEffect.NavigateSeries -> {
            navController.navigate(SeriesRoute(effect.seriesId))
        }

        HomeEffect.NavigateToProfile -> { /* TODO: Navigate to profile */
        }

        HomeEffect.NavigateToSeeAllAiringToday -> {}
        HomeEffect.NavigateToSeeAllOnTv -> {}
    }
}

private fun effectDiscoverHandler(
    effect: DiscoverEffect, navController: NavController
) {

    when (effect) {
        is DiscoverEffect.NavigateMovie -> {
            navController.navigate(MovieRoute(effect.movieId))
        }

        is DiscoverEffect.NavigateSeries -> {
            navController.navigate(SeriesRoute(effect.seriesId))
        }

        is DiscoverEffect.NavigateToDiscover -> {
            navController.navigate("discover/${effect.type.name}/${effect.mediaType.name}")
        }

        is DiscoverEffect.NavigateToSeeAllFreeToWatch -> {
            navController.navigate("discover/${DiscoverType.FREE_TO_WATCH.name}/${effect.mediaType.name}")
        }

        is DiscoverEffect.NavigateToSeeAllTopRating -> {
            navController.navigate("discover/${DiscoverType.TOP_RATING.name}/${effect.mediaType.name}")
        }

        is DiscoverEffect.NavigateToSeeAllUpcoming -> {
            navController.navigate("discover/${DiscoverType.UPCOMING.name}/${effect.mediaType.name}")
        }

        is DiscoverEffect.NavigateToSeeAllTrending -> {
            navController.navigate("discover/${DiscoverType.TRENDING.name}/${effect.mediaType.name}")
        }
    }
}
