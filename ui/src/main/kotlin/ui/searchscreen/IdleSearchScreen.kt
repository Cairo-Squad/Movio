package ui.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.component.MovieCard
import com.cairosquad.design_system.component.SectionHeader
import searchviewmodel.MovieUiState

@Composable
fun IdleSearchScreenContent(
    forYouMovies: List<MovieUiState>,
    exploreMoreMovies: List<MovieUiState>,
    onMovieClick: (MovieUiState) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        SectionHeader(title = "For you", actionText = "See all")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forYouMovies) { movie ->
                MovieCard(
                    title = movie.title,
                    vote = movie.rating,
                    imgUrl = movie.posterPath,
                    modifier = Modifier.clickable { onMovieClick(movie) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(title = "Explore more", actionText = "See all")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(exploreMoreMovies.chunked(2)) { rowMovies ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowMovies.forEach { movie ->
                        MovieCard(
                            title = movie.title,
                            vote = movie.rating,
                            imgUrl = movie.posterPath,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onMovieClick(movie) }
                        )
                    }

                    // Fill empty space if there's only 1 card in the row
                    if (rowMovies.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}






