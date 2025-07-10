package com.cairosquad.movio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cairosquad.ui.home.HomeScreen
import com.cairosquad.ui.library.LibraryScreen
import com.cairosquad.ui.more.MoreScreen
import com.cairosquad.ui.search.SearchScreen
import com.cairosquad.ui.splash.SplashScreen

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