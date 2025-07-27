package com.cairosquad.ui.navigation

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.AppScreen
import com.cairosquad.ui.auth.ForgetPasswordWebViewScreen
import com.cairosquad.ui.auth.LoginScreen
import com.cairosquad.ui.auth.SignUpWebViewScreen
import com.cairosquad.ui.details.EpisodesScreen
import com.cairosquad.ui.details.MovieScreen
import com.cairosquad.ui.details.ReviewsScreen
import com.cairosquad.ui.details.SeasonsScreen
import com.cairosquad.ui.details.SeriesScreen
import com.cairosquad.ui.details.TopCastScreen
import com.cairosquad.ui.details.artist.ArtistScreen
import com.cairosquad.ui.details.similar_movies.SimilarMoviesScreen
import com.cairosquad.ui.details.similar_series.SimilarSeriesScreen
import com.cairosquad.ui.search.ForYouScreen
import com.cairosquad.ui.see_all_screen.SeeAllScreen
import com.cairosquad.ui.splash.SplashScreen
import com.cairosquad.viewmodel.auth_gate.AuthGate
import kotlinx.coroutines.launch
import org.koin.compose.getKoin


@Composable
fun AppNavigation(
    authGate: AuthGate = getKoin().get()
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    SplashScreen(
                        onNavigateNext = {
                            coroutineScope.launch {
                                val route = if (authGate.isUserLoggedIn()) AppRoute else LoginRoute
                                navController.navigate(route) {
                                    popUpTo(SplashRoute) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                } else {

                    LaunchedEffect(Unit) {
                        val route = if (authGate.isUserLoggedIn()) AppRoute else LoginRoute
                        navController.navigate(route) {
                            popUpTo(SplashRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
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
                AppScreen()
            }
            composable<MovieRoute> { backStackEntry ->
                MovieScreen(
                    movieId = backStackEntry.toRoute<MovieRoute>().movieId
                )
            }
            composable<SeriesRoute> { backStackEntry ->
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
        }
    }
}

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("No nav controller provided") }