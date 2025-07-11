package com.cairosquad.ui.search.content

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.ArtistCard
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.MovieCard
import com.cairosquad.design_system.component.MovieCardSize
import com.cairosquad.design_system.component.StateMessage
import com.cairosquad.design_system.component.TopBar
import com.cairosquad.design_system.text_style.defaultTextStyle
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.searchviewmodel.SearchInteractionListener
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState

@Composable
fun SearchResultContent(
    state: SearchUiState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    DisposableEffect(backPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener.onBackClicked()
            }
        }
        backPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(bottom = 12.dp),
            value = state.query,
            onValueChange = listener::onQueryTextChanged,
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
            onFocusChanged = {
                if (it) {
                    listener.onClickSearchTextField()
                }
            },
            readOnly = true
        )

        TopBar(
            modifier = Modifier.padding(bottom = 12.dp),
            tabs = listOf(
                stringResource(R.string.top_Results),
                stringResource(R.string.movies),
                stringResource(R.string.series),
                stringResource(R.string.artists),
            ),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        when (selectedTabIndex) {
            0 -> {
                SearchResultText(noOfResults = state.movies.size)
                Spacer(modifier = Modifier.height(16.dp))
                AllResultsTabContent(topResults = state.movies)
            }

            1 -> {
                SearchResultText(noOfResults = state.movies.size)
                Spacer(modifier = Modifier.height(16.dp))
                MoviesTabContent(movies = state.movies)
            }

            2 -> {
                SearchResultText(noOfResults = state.series.size)
                Spacer(modifier = Modifier.height(16.dp))
                SeriesTabContent(series = state.series)
            }

            3 -> {
                SearchResultText(noOfResults = state.artists.size)
                Spacer(modifier = Modifier.height(16.dp))
                ArtistsTabContent(artists = state.artists)
            }
        }
    }
}

@Composable
private fun AllResultsTabContent(topResults: List<SearchUiState.MovieUiState>) {
    if (topResults.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
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
private fun MoviesTabContent(movies: List<SearchUiState.MovieUiState>) {
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
private fun SeriesTabContent(series: List<SearchUiState.SeriesUiState>) {
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
private fun ArtistsTabContent(artists: List<SearchUiState.ArtistUiState>) {
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicText(
            text = stringResource(R.string.search_result),
            style = defaultTextStyle.title.mediumMedium16.copy(
                Theme.color.surfaces.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.size(4.dp))
        BasicText(
            text = "(${stringResource(R.string.number_of_items, noOfResults)})",
            style = defaultTextStyle.label.smallRegular14.copy(
                Theme.color.surfaces.onSurfaceVariant
            )
        )
    }
}