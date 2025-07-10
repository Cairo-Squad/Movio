package ui.searchscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.ArtistCard
import com.cairosquad.design_system.component.MovieCard
import com.cairosquad.design_system.component.MovieCardSize
import com.cairosquad.design_system.component.StateMessage
import com.cairosquad.design_system.component.TopBar
import com.cairosquad.design_system.text_style.defaultTextStyle

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    topResults: List<MovieUiState> = emptyList(),
    movies: List<MovieUiState> = emptyList(),
    series: List<SeriesUiState> = emptyList(),
    artists: List<ArtistUiState> = emptyList()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        TopBar(
            tabs = listOf(
                stringResource(R.string.top_Results),
                stringResource(R.string.movies),
                stringResource(R.string.series),
                stringResource(R.string.artists),
            ),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTabIndex) {
            0 -> {
                SearchResultText(noOfResults = topResults.size)
                Spacer(modifier = Modifier.height(16.dp))
                AllResultsTabContent(topResults = topResults)
            }

            1 -> {
                SearchResultText(noOfResults = movies.size)
                Spacer(modifier = Modifier.height(16.dp))
                MoviesTabContent(movies = movies)
            }

            2 -> {
                SearchResultText(noOfResults = series.size)
                Spacer(modifier = Modifier.height(16.dp))
                SeriesTabContent(series = series)
            }

            3 -> {
                SearchResultText(noOfResults = artists.size)
                Spacer(modifier = Modifier.height(16.dp))
                ArtistsTabContent(artists = artists)
            }
        }
    }
}

@Composable
private fun AllResultsTabContent(topResults: List<MovieUiState>) {
    if (topResults.isEmpty()) {
        StateMessage(
            imageDrawable = R.drawable.no_result,
            titleId = R.string.no_results_found,
            descriptionId = R.string.no_results_found_description
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(topResults) { result ->
                MovieCard(
                    title = result.title,
                    vote = result.rating,
                    imgUrl = result.posterPath,
                    modifier = Modifier.padding(bottom = 16.dp),
                    cardSize = MovieCardSize.Small
                )
            }
        }
    }
}

@Composable
private fun MoviesTabContent(movies: List<MovieUiState>) {
    if (movies.isEmpty()) {
        StateMessage(
            imageDrawable = R.drawable.no_result,
            titleId = R.string.no_results_found,
            descriptionId = R.string.no_results_found_description
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                MovieCard(
                    title = movie.title,
                    vote = movie.rating,
                    imgUrl = movie.posterPath,
                    modifier = Modifier.padding(bottom = 16.dp),
                    cardSize = MovieCardSize.Small
                )
            }
        }
    }
}

@Composable
private fun SeriesTabContent(series: List<SeriesUiState>) {
    if (series.isEmpty()) {
        StateMessage(
            imageDrawable = R.drawable.no_result,
            titleId = R.string.no_results_found,
            descriptionId = R.string.no_results_found_description
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(series) { series ->
                MovieCard(
                    title = series.title,
                    vote = series.rating,
                    imgUrl = series.posterPath,
                    modifier = Modifier.padding(bottom = 16.dp),
                    cardSize = MovieCardSize.Small
                )
            }
        }
    }
}

@Composable
private fun ArtistsTabContent(artists: List<ArtistUiState>) {
    if (artists.isEmpty()) {
        StateMessage(
            imageDrawable = R.drawable.no_result,
            titleId = R.string.no_results_found,
            descriptionId = R.string.no_results_found_description
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(artists) { artist ->
                ArtistCard(
                    name = artist.name,
                    imgUrl = artist.photoPath,
                )
            }
        }
    }
}

@Composable
fun SearchResultText(noOfResults: Int) {
    Row {
        BasicText(
            text = stringResource(R.string.search_result),
            style = defaultTextStyle.title.mediumMedium16
        )
        Spacer(modifier = Modifier.size(4.dp))
        BasicText(
            text = stringResource(R.string.number_of_items, noOfResults),
            style = defaultTextStyle.title.mediumMedium14
        )
    }
}

data class MovieUiState(
    val title: String,
    val rating: Float,
    val posterPath: String
)

data class ArtistUiState(
    val name: String,
    val photoPath: String
)

data class SeriesUiState(
    val title: String,
    val rating: Float,
    val posterPath: String
)
