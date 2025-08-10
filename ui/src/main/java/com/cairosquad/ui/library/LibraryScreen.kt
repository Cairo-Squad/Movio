package com.cairosquad.ui.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.library.content.LibraryScreenContent
import com.cairosquad.ui.navigation.ListRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.navigation.ViewAllFavoritesRoute
import com.cairosquad.ui.navigation.ViewAllHistoryRoute
import com.cairosquad.ui.navigation.ViewAllListsRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.LibraryEffect
import com.cairosquad.viewmodel.library.LibraryViewModel

@Composable
fun LibraryScreen() {
    val viewModel: LibraryViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.loadScreenState()
    }

    ObserveAsEffect(viewModel.effect) {
        when (it) {
            LibraryEffect.NavigateToFavorites -> {
                navController.navigate(ViewAllFavoritesRoute)
            }

            LibraryEffect.NavigateToHistory -> {
                navController.navigate(ViewAllHistoryRoute)
            }

            LibraryEffect.NavigateToLists -> {
                navController.navigate(ViewAllListsRoute)
            }

            LibraryEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute)
            }

            is LibraryEffect.NavigateToListDetails -> {
                navController.navigate(ListRoute(it.listId, it.listName))
            }

            is LibraryEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }

            is LibraryEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }

        }
    }

    LibraryScreenContent(
        screenState = screenState,
        listener = viewModel
    )
}