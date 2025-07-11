package com.cairosquad.movio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cairosquad.ui.splash.SplashScreen
import ui.AppScreen

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