package com.cairosquad.movio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.AppScreen
import ui.splash.SplashScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ) {
        composable<SplashRoute> {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(AppRoute)
                }
            )
        }
        composable<AppRoute> {
            AppScreen()
        }
    }
}