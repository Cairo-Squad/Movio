package com.cairosquad.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.AppScreen
import com.cairosquad.ui.auth.content.ForgetPasswordWebViewScreen
import com.cairosquad.ui.auth.LoginScreen
import com.cairosquad.ui.auth.content.SignUpWebViewScreen
import com.cairosquad.ui.details.Constants
import com.cairosquad.ui.details.artist.ArtistScreen
import com.cairosquad.ui.details.episodes.EpisodesScreen
import com.cairosquad.ui.details.movie.MovieScreen
import com.cairosquad.ui.details.reviews.ReviewsScreen
import com.cairosquad.ui.details.seasons.SeasonsScreen
import com.cairosquad.ui.details.series.SeriesScreen
import com.cairosquad.ui.details.similar_movies.SimilarMoviesScreen
import com.cairosquad.ui.details.similar_series.SimilarSeriesScreen
import com.cairosquad.ui.details.top_cast.TopCastScreen
import com.cairosquad.ui.library.content.ListScreen
import com.cairosquad.ui.library.content.ViewAllFavorite
import com.cairosquad.ui.library.content.ViewAllHistory
import com.cairosquad.ui.library.content.ViewAllLists
import com.cairosquad.ui.onboarding.OnboardingScreen
import com.cairosquad.ui.rated.MyRatingsScreen
import com.cairosquad.ui.search.ForYouScreen
import com.cairosquad.ui.see_all_screen.SeeAllScreen
import com.cairosquad.ui.splash.SplashScreen
import com.cairosquad.viewmodel.auth_gate.AuthGate
import kotlinx.coroutines.launch


@Composable
fun AppNavigation(
    authGate: AuthGate
) {

    val navController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            modifier = Modifier.background(Theme.color.surfaces.surface),
            navController = navController,
            startDestination = SplashRoute
        ) {
            composable<SplashRoute> {
                SplashScreen(
                    onNavigateNext = {
                        coroutineScope.launch {
                            val secondRoute = if (authGate.isUserLoggedIn()) AppRoute else LoginRoute
                            val firstRoute = if (authGate.isOnboardingStateComplete()) secondRoute else OnboardingRoute
                            navController.navigate(firstRoute) {
                                popUpTo(SplashRoute) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable<OnboardingRoute> {
                OnboardingScreen(
                    navigateToAuthOrHome = {
                        coroutineScope.launch {
                            val route = if (authGate.isUserLoggedIn()) AppRoute else LoginRoute
                            navController.navigate(route) {
                                popUpTo(OnboardingRoute) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable<LoginRoute> {
                LoginScreen()
            }

            composable<ForgetPasswordWebViewRoute> { backStackEntry ->
                ForgetPasswordWebViewScreen(
                    url = backStackEntry.toRoute<ForgetPasswordWebViewRoute>().url
                )
            }

            composable<SignUpWebViewRoute> { backStackEntry ->
                SignUpWebViewScreen(
                    url = backStackEntry.toRoute<SignUpWebViewRoute>().url
                )
            }

            composable<AppRoute> {
                var isUserLoggedIn by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    isUserLoggedIn = authGate.isUserLoggedIn()
                }

                AppScreen(isUserLoggedIn = isUserLoggedIn)
            }

            composable<MovieRoute>(
                deepLinks = listOf(
                    navDeepLink<MovieRoute>(basePath = Constants.MOVIE_DEEP_URL)
                )
            ) { backStackEntry ->
                MovieScreen(
                    movieId = backStackEntry.toRoute<MovieRoute>().movieId
                )
            }
            composable<SeriesRoute>(
                deepLinks = listOf(
                    navDeepLink<SeriesRoute>(basePath = Constants.SERIES_DEEP_URL)
                )
            ){ backStackEntry ->
                SeriesScreen(
                    seriesId = backStackEntry.toRoute<SeriesRoute>().seriesId
                )
            }
            composable<ArtistRoute> { backStackEntry ->
                ArtistScreen(
                    artistId = backStackEntry.toRoute<ArtistRoute>().artistId,
                    navController = navController
                )
            }
            composable<SimilarMovieRoute> { backStackEntry ->
                SimilarMoviesScreen(
                    movieId = backStackEntry.toRoute<SimilarMovieRoute>().movieId,
                    navController = navController
                )
            }
            composable<SimilarSeriesRoute> { backStackEntry ->
                SimilarSeriesScreen(
                    seriesId = backStackEntry.toRoute<SimilarSeriesRoute>().seriesId, navController
                )
            }
            composable<TopCastRoute> { backStackEntry ->
                TopCastScreen(
                    mediaId = backStackEntry.toRoute<TopCastRoute>().mediaId,
                    isMovie = backStackEntry.toRoute<TopCastRoute>().isMovie,
                    navController = navController
                )
            }
            composable<ReviewsRoute> { backStackEntry ->
                ReviewsScreen(
                    mediaId = backStackEntry.toRoute<ReviewsRoute>().mediaId,
                    isMovie = backStackEntry.toRoute<ReviewsRoute>().isMovie,
                )
            }
            composable<SeasonsRoute> { backStackEntry ->
                SeasonsScreen(
                    seriesId = backStackEntry.toRoute<SeasonsRoute>().seriesId
                )
            }
            composable<EpisodesRoute> { backStackEntry ->
                EpisodesScreen(
                    seriesId = backStackEntry.toRoute<EpisodesRoute>().seriesId,
                    seasonNumber = backStackEntry.toRoute<EpisodesRoute>().seasonNumber
                )
            }
            composable<ForYouRoute> {
                ForYouScreen()
            }
            composable<SeeAllScreenRoute> { backStackEntry ->
                SeeAllScreen(
                    contentType = backStackEntry.toRoute<SeeAllScreenRoute>().contentType,
                    mediaType = backStackEntry.toRoute<SeeAllScreenRoute>().mediaType
                )
            }
            composable<ViewAllListsRoute> {
                ViewAllLists()
            }
            composable<ViewAllFavoritesRoute> {
                ViewAllFavorite()
            }
            composable<ViewAllHistoryRoute> {
                ViewAllHistory()
            }
            composable<ListRoute> {backStackEntry ->
                ListScreen(
                    listId = backStackEntry.toRoute<ListRoute>().listId,
                    listName = backStackEntry.toRoute<ListRoute>().listName
                )
            }
            composable<MyRatingsRoute> {
                MyRatingsScreen()
            }
        }
    }
}

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("No nav controller provided") }
