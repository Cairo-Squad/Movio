package com.cairosquad.ui.details.similar_movies.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesScreenState
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel

@Composable
fun SimilarMoviesGrid(
    state: SimilarMoviesScreenState,
    viewModel: SimilarMoviesViewModel,
    movieId: Long
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (state.screenStatus) {
            SimilarMoviesScreenState.ScreenStatus.LOADING -> {
                items(20) { LoadingMovieCard() }
            }

            SimilarMoviesScreenState.ScreenStatus.SUCCESS -> {
                items(state.movies) { movie ->
                    MovieCard(
                        title = movie.title,
                        vote = movie.rating,
                        imgUrl = movie.posterUrl,
                        modifier = Modifier.clickable {
                            viewModel.onMovieClick(movie.id)
                        },
                        width = null,
                        aspectRatio = 0.745F,
                    )
                }
            }

            SimilarMoviesScreenState.ScreenStatus.ERROR -> {
                item { DetailsFailContent(onTryAgainClick = { viewModel.onRefresh(movieId) }) }
            }
        }
    }
}