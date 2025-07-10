package com.cairosquad.movio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.home.HomeScreen
import ui.library.LibraryScreen
import ui.more.MoreScreen
import ui.searchscreen.SearchScreen
import ui.splash.SplashScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SearchRoute
    ) {
        composable<SplashRoute> {
            SplashScreen()
        }
        composable<HomeRoute> {
            HomeScreen()
        }
        composable<SearchRoute> {
            SearchScreen()
        }
        composable<LibraryRoute> {
            LibraryScreen()
        }
        composable<MoreRoute> {
            MoreScreen()
        }
    }
}