package com.cairosquad.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.MovieCard
import com.cairosquad.design_system.component.MovieCardSize
import com.cairosquad.design_system.component.SectionHeader
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState.MovieUiState

@Composable
fun ExploreScreenContent(
    forYouMovies: List<MovieUiState>,
    exploreMoreMovies: List<MovieUiState>,
    onMovieClick: (MovieUiState) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        SectionHeader(
            title = "For you",
            actionText = "See all",
            actionIcon = ImageVector.vectorResource(R.drawable.arrow)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forYouMovies) { movie ->
                MovieCard(
                    modifier = Modifier
                        .clickable { onMovieClick(movie) }
                        .width(124.dp),
                    title = movie.title,
                    vote = movie.rating,
                    imgUrl = movie.posterPath,
                    cardSize = MovieCardSize.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(
            title = "Explore more", actionText = "See all",
            actionIcon = ImageVector.vectorResource(R.drawable.arrow)
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            columns = GridCells.Adaptive(minSize = 158.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(exploreMoreMovies) { movie ->

                MovieCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onMovieClick(movie) },
                    title = movie.title,
                    vote = movie.rating,
                    imgUrl = movie.posterPath,
                    cardSize = MovieCardSize.Large
                )
            }
        }
    }
}
