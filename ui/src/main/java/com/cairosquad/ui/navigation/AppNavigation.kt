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
import com.cairosquad.ui.details.artist.ArtistScreen
import com.cairosquad.ui.details.MovieScreen
import com.cairosquad.ui.details.ReviewsScreen
import com.cairosquad.ui.details.SeasonScreen
import com.cairosquad.ui.details.SeasonsScreen
import com.cairosquad.ui.details.SeriesScreen
import com.cairosquad.ui.details.SimilarMoviesScreen
import com.cairosquad.ui.details.SimilarSeriesScreen
import com.cairosquad.ui.details.TopCastScreen
import com.cairosquad.ui.search.ForYouScreen
import com.cairosquad.ui.splash.SplashScreen
import com.cairosquad.ui.AppScreen



@Composable
fun AppNavigation() {

    val navController = rememberNavController()

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
                        navController.popBackStack()
                        navController.navigate(AppRoute)
                    }
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
                    navController=navController
                )
            }
            composable<SimilarMovieRoute> { backStackEntry ->
                SimilarMoviesScreen(
                    movieId = backStackEntry.toRoute<SimilarMovieRoute>().movieId
                )
            }
            composable<SimilarSeriesRoute> { backStackEntry ->
                SimilarSeriesScreen(
                    seriesId = backStackEntry.toRoute<SimilarSeriesRoute>().seriesId
                )
            }
            composable<TopCastRoute> { backStackEntry ->
                TopCastScreen(
                    mediaId = backStackEntry.toRoute<TopCastRoute>().mediaId,
                    isMovie = backStackEntry.toRoute<TopCastRoute>().isMovie
                )
            }
            composable<ReviewsRoute> { backStackEntry ->
                ReviewsScreen(
                    mediaId = backStackEntry.toRoute<ReviewsRoute>().mediaId,
                    isMovie = backStackEntry.toRoute<ReviewsRoute>().isMovie
                )
            }
            composable<SeasonsRoute> { backStackEntry ->
                SeasonsScreen(
                    seriesId = backStackEntry.toRoute<SeasonsRoute>().seriesId
                )
            }
            composable<SeasonRoute> { backStackEntry ->
                SeasonScreen(
                    seriesId = backStackEntry.toRoute<SeasonRoute>().seriesId,
                    seasonNumber = backStackEntry.toRoute<SeasonRoute>().seasonNumber
                )
            }
            composable<ForYouRoute> {
                ForYouScreen()
            }
        }
    }
}

val LocalNavController = staticCompositionLocalOf<NavHostController> { error("No nav controller provided") }
