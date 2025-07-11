package com.cairosquad.movio.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.splash.SplashScreen
import ui.AppScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

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
            AppScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}