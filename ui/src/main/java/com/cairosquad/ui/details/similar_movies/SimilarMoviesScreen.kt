package com.cairosquad.ui.details.similar_movies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesEffect
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesScreenState
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
            .windowInsetsPadding(WindowInsets.systemBars)
    ){
        AppBar(
            title = stringResource(R.string.similar_movies),
            onBackButtonClicked = { viewModel.onClickBack() },
        )
        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (state.screenStatus) {
                SimilarMoviesScreenState.ScreenStatus.LOADING -> {
                    items(20) {
                        LoadingMovieCard()
                    }
                }

                SimilarMoviesScreenState.ScreenStatus.SUCCESS -> {
                    items(state.movies) { movie ->
                        MovieCard(
                            title = movie.title,
                            vote = movie.rating,
                            imgUrl = movie.posterUrl,
                            modifier = Modifier
                                .clickable {
                                    viewModel.onMovieClicked(movie.id)
                                },
                            width = null,
                            aspectRatio = 0.745F,
                        )
                    }
                }

                SimilarMoviesScreenState.ScreenStatus.ERROR -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            StateMessage(
                                imageDrawable = R.drawable.no_internet,
                                titleId = R.string.no_internet_connection,
                                descriptionId = R.string.internet_is_not_available_description
                            )
                        }
                    }
                }
            }
        }
    }
}