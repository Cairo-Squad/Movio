package com.cairosquad.ui.details.similar_movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.details.similar_movies.content.SimilarMoviesGrid
import com.cairosquad.ui.details.similar_movies.content.SimilarMoviesScreenEffects
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel

@Composable
fun SimilarMoviesScreen(
    movieId: Long,
    navController: NavController,
    viewModel: SimilarMoviesViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()
    LaunchedEffect(movieId) {
        viewModel.fetchSimilarMovies(movieId)
    }
    SimilarMoviesScreenEffects(viewModel, navController)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        AppBar(
            title = stringResource(R.string.similar_movies),
            onBackButtonClicked = { viewModel.onClickBack() },
        )
        SimilarMoviesGrid(state, viewModel, movieId)
    }
}