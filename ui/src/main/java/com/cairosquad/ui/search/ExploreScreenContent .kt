package com.cairosquad.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        item {
            SectionHeader(
                title = "For you",
                actionText = "See all",
                actionIcon = ImageVector.vectorResource(R.drawable.arrow)
            )
        }
        item {
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
        }
        item {
            SectionHeader(
                title = "Explore more", actionText = "See all",
                actionIcon = ImageVector.vectorResource(R.drawable.arrow)
            )

        }
        item {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(max = ((exploreMoreMovies.size / 2 + 1) * 240).dp),
                columns = GridCells.Adaptive(minSize = 158.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(exploreMoreMovies) { movie ->

                    MovieCard(
                        modifier = Modifier
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
}
