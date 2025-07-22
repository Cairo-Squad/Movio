package com.cairosquad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.cairosquad.ui.home.content.HomeScreenContent
import com.cairosquad.ui.navigation.FreeToWatchRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MoreRecommendedRoute
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.navigation.TopRatingRoute
import com.cairosquad.ui.navigation.TrendingRoute
import com.cairosquad.ui.navigation.UpComingRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.home.HomeEffect
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {

    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect){ effect -> effectHandler(effect, navController) }

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
        is HomeEffect.NavigateMovie -> { navController.navigate(MovieRoute(effect.movieId)) }
        is HomeEffect.NavigateSeries -> { navController.navigate(SeriesRoute(effect.seriesId)) }
        HomeEffect.NavigateToProfile -> { /* TODO: Navigate to profile */ }
        HomeEffect.NavigateToSeeAllAiringToday -> {  }
        HomeEffect.NavigateToSeeAllFreeToWatch -> {  navController.navigate(FreeToWatchRoute) }
        is HomeEffect.NavigateToSeeAllMoreRecommended -> {  navController.navigate(
            MoreRecommendedRoute) }
        HomeEffect.NavigateToSeeAllOnTv -> {  }
        is HomeEffect.NavigateToSeeAllTopRated -> {  navController.navigate(TopRatingRoute)}
        HomeEffect.NavigateToSeeAllTrending -> {navController.navigate(TrendingRoute)  }
        HomeEffect.NavigateToSeeAllUpcoming -> { navController.navigate(UpComingRoute) }
    }
}
