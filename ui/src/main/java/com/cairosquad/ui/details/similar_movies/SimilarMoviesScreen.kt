package com.cairosquad.ui.details.similar_movies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesEffect
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SimilarMoviesScreen(
    movieId: Long,
    navController: NavController,
    viewModel: SimilarMoviesViewModel = koinViewModel()
) {
    val state by viewModel.screenState.collectAsState()
    LaunchedEffect(movieId) {
        viewModel.fetchSimilarMovies(movieId)
    }
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            is SimilarMoviesEffect.NavigateBack -> navController.popBackStack()

            is SimilarMoviesEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ){
        AppBar(
            title = stringResource(R.string.similar_movies),
            onBackButtonClicked = { viewModel.onClickBack() },
        )
        LazyVerticalGrid(
            modifier = Modifier.padding(16.dp),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.movies) { movie ->
                MovieCard(
                    title = movie.title,
                    vote = movie.rating,
                    imgUrl = movie.posterUrl,
                    modifier = Modifier
                        .clickable {
                        viewModel.onMovieClicked(movieId)
                    },
                    width = null,
                    aspectRatio = 0.745F,
                )
            }

        }

    }

}