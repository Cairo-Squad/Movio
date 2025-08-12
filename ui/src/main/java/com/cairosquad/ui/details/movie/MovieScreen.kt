package com.cairosquad.ui.details.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.ui.details.movie.content.MovieContent
import com.cairosquad.ui.details.movie.content.MovieScreenBottomSheets
import com.cairosquad.ui.details.movie.content.MovieScreenEffects
import com.cairosquad.ui.details.movie.content.MovieScreenSnackBar
import com.cairosquad.viewmodel.details.movie.MovieViewModel

@Composable
fun MovieScreen(
    movieId: Long,
) {
    val viewModel: MovieViewModel =
        hiltViewModel<MovieViewModel, MovieViewModel.Factory> { factory ->
            factory.create(movieId)
        }
    val state by viewModel.screenState.collectAsState()

    MovieScreenEffects(viewModel, movieId, state)

    Box {
        MovieContent(uiState = state, interactionListener = viewModel)
        MovieScreenBottomSheets(state, viewModel, movieId)
        MovieScreenSnackBar(state)
    }
}