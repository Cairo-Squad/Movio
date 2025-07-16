package com.cairosquad.ui.search.content

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.text_style.defaultTextStyle
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchResultContent(
    state: SearchScreenState,
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
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
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

        TabRow(
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
                AllResultsTabContent(state = state, listener = listener)
            }

            1 -> {
                SearchResultText(noOfResults = state.movies.size)
                MoviesTabContent(state = state, listener = listener)
            }

            2 -> {
                SearchResultText(noOfResults = state.series.size)
                SeriesTabContent(state = state, listener = listener)
            }

            3 -> {
                SearchResultText(noOfResults = state.artists.size)
                ArtistsTabContent(state = state, listener = listener)
            }
        }
    }
}

@Composable
private fun AllResultsTabContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.movies.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
    }

    AnimatedVisibility(
        visible = state.movies.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.movies) { result ->
                MovieCard(
                    modifier = Modifier
                        .clickable(onClick = { listener.onMovieClicked(result.id) }),
                    title = result.title,
                    vote = result.rating,
                    imgUrl = result.posterPath,
                    width = null,
                    aspectRatio = 0.743f
                )
            }
        }
    }
}

@Composable
private fun MoviesTabContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.movies.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
    }

    AnimatedVisibility(
        visible = state.movies.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.movies) { movie ->
                MovieCard(
                    modifier = Modifier
                        .clickable(onClick = { listener.onMovieClicked(movie.id) }),
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

@Composable
private fun SeriesTabContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.series.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
    }

    AnimatedVisibility(
        visible = state.series.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.series) { series ->
                MovieCard(
                    modifier = Modifier
                        .clickable(onClick = { listener.onSeriesClicked(series.id) }),
                    title = series.title,
                    vote = series.rating,
                    imgUrl = series.posterPath,
                    width = null,
                    aspectRatio = 0.743f
                )
            }
        }
    }
}

@Composable
private fun ArtistsTabContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.artists.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
    }

    AnimatedVisibility(
        visible = state.artists.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.artists) { artist ->
                ArtistCard(
                    modifier = Modifier
                        .clickable(onClick = { listener.onArtistClicked(artist.id) }),
                    name = artist.name,
                    imgUrl = artist.photoPath,
                )
            }
        }
    }
}

@Composable
fun SearchResultText(
    noOfResults: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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