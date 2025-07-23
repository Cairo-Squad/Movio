package com.cairosquad.ui.search.content.for_you

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.foryou.ForYouInteractionListener
import com.cairosquad.viewmodel.foryou.ForYouState

@Composable
fun MoviesGridContent(
    movies: LazyPagingItems<ForYouState.MovieUiState>,
    listener: ForYouInteractionListener,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(movies.itemCount) { index ->
            movies[index]?.let { movie ->
                MovieCard(
                    modifier = Modifier.clickable {
                        listener.onMovieClicked(movie.id)
                    },
                    title = movie.title,
                    vote = movie.rating,
                    imgUrl = movie.posterPath,
                    width = null,
                    aspectRatio = 0.743f
                )
            }
        }
    }
}