package com.cairosquad.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.movie.MovieViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieScreen(
    movieId: Long,
    viewModel: MovieViewModel = koinViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.screenState.collectAsState()


    MovieContent(
        state = state,
        interactionListener = viewModel,
    )
}

@Composable
fun MovieContent(
    state: MovieScreenState,
    interactionListener: MovieInteractionListener,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {

    }
}