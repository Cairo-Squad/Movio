package com.cairosquad.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            modifier = Modifier.background(Theme.color.surfaces.surface),
            navController = navController,
            startDestination = AppRoute
        ) {

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
        }
    }
}

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("No nav controller provided") }
