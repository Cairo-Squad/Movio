package com.cairosquad.ui.details.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.ui.details.movie.content.MovieContent
import com.cairosquad.ui.details.movie.composable.MovieScreenBottomSheets
import com.cairosquad.ui.details.movie.content.MovieScreenEffects
import com.cairosquad.ui.details.movie.composable.MovieScreenSnackBar
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

    MovieScreenEffects(viewModel, state)

    Box {
        MovieContent(state = state, listener = viewModel)
        MovieScreenBottomSheets(state, viewModel)
        MovieScreenSnackBar(state)
    }
}