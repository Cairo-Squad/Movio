package com.cairosquad.ui.search.content

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.text_style.defaultTextStyle
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchResultContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val movies = state.movies.collectAsLazyPagingItems()
    val artists = state.artists.collectAsLazyPagingItems()
    val series = state.series.collectAsLazyPagingItems()
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

    val selectedTabIndex = state.selectedTabIndex

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
            placeholder = stringResource(R.string.search_with_dotes_ahead),
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
                onTabSelected = { listener.onTabSelected(it) }
            )

            when (selectedTabIndex) {
                0 -> {
                    SearchResultText(noOfResults = movies.itemCount)
                    AllResultsTabContent(movies = movies, listener = listener, state = state)
                }

                1 -> {
                    SearchResultText(noOfResults = movies.itemCount)
                    MoviesTabContent(movies = movies, listener = listener, state = state)
                }

                2 -> {
                    SearchResultText(noOfResults = series.itemCount)
                    SeriesTabContent(series = series, listener = listener, state = state)
                }

                3 -> {
                    SearchResultText(noOfResults = artists.itemCount)
                    ArtistsTabContent(artist = artists, listener = listener, state = state)
                }
            }
    }
}

@Composable
private fun AllResultsTabContent(
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val isLoading = movies.loadState.refresh is LoadState.Loading
    val isError = movies.loadState.refresh is LoadState.Error
    val isEmpty = movies.itemCount == 0 && !isLoading && !isError
    val error = (movies.loadState.refresh as? LoadState.Error)?.error

    LaunchedEffect(error) {
        if (error != null) {
            listener.onTabPagingError(error)
        }
    }

    when {
        isLoading -> {
            SearchLoadingContent(
                state = state,
                listener = listener,
                modifier = modifier
            )
        }
        isError ->{
            SearchResultFail(
                errorStatus = state.errorStatus
            )
        }
        isEmpty -> {
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

        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 101.33.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(movies.itemCount) { index ->
                    movies[index]?.let { result ->
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
    }
}


@Composable
private fun MoviesTabContent(
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    listener: SearchInteractionListener,
    state: SearchScreenState,
    modifier: Modifier = Modifier
) {
    val isLoading = movies.loadState.refresh is LoadState.Loading
    val isError = movies.loadState.refresh is LoadState.Error
    val isEmpty = movies.itemCount == 0 && !isLoading && !isError

    when {
        isLoading -> {
            SearchLoadingContent(
                state = state,
                listener = listener,
                modifier = modifier
            )
        }
        isError ->{
            SearchResultFail(
                errorStatus = state.errorStatus
            )
        }
        isEmpty -> {
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

        else -> {
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
    }
}


@Composable
private fun SeriesTabContent(
    series: LazyPagingItems<SearchScreenState.SeriesUiState>,
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val isLoading = series.loadState.refresh is LoadState.Loading
    val isError = series.loadState.refresh is LoadState.Error
    val isEmpty = series.itemCount == 0 && !isLoading && !isError
    val error = (series.loadState.refresh as? LoadState.Error)?.error

    LaunchedEffect(error) {
        if (error != null) {
            listener.onTabPagingError(error)
        }
    }

    when {
        isLoading -> {
            SearchLoadingContent(
                state = state,
                listener = listener,
                modifier = modifier
            )
        }
        isError ->{
            SearchResultFail(
                errorStatus = state.errorStatus
            )
        }
        isEmpty -> {
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

        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 101.33.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(series.itemCount) { index ->
                    series[index]?.let { series ->
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
    }
}

@Composable
private fun ArtistsTabContent(
    artist: LazyPagingItems<SearchScreenState.ArtistUiState>,
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val isLoading = artist.loadState.refresh is LoadState.Loading
    val isError = artist.loadState.refresh is LoadState.Error
    val isEmpty = artist.itemCount == 0 && !isLoading && !isError
    val error = (artist.loadState.refresh as? LoadState.Error)?.error

    LaunchedEffect(error) {
        if (error != null) {
            listener.onTabPagingError(error)
        }
    }

    when {
        isLoading -> {
            SearchLoadingContent(
                state = state,
                listener = listener,
                modifier = modifier
            )
        }
        isError ->{
            SearchResultFail(
                errorStatus = state.errorStatus
            )
        }
        isEmpty -> {
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

        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 101.33.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(artist.itemCount) { index ->
                    artist[index]?.let { artist ->
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
    }
}

@Composable
private fun SearchResultFail(
    errorStatus: ErrorStatus?
){
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        StateMessage(
            imageDrawable = when (errorStatus) {
                ErrorStatus.NO_INTERNET -> R.drawable.no_internet
                ErrorStatus.NETWORK_ERROR -> R.drawable.no_result
                ErrorStatus.UNKNOWN_ERROR -> R.drawable.no_result
                null -> R.drawable.no_result
                ErrorStatus.UNAUTHORIZED -> R.drawable.no_result
                ErrorStatus.EMPTY -> R.drawable.no_result
                ErrorStatus.PARSING_ERROR -> R.drawable.no_result
            },
            titleId = when (errorStatus) {
                ErrorStatus.NO_INTERNET -> R.string.no_internet_connection
                ErrorStatus.NETWORK_ERROR -> R.string.an_error_occured_while_getting_results
                ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred
                null -> R.string.an_unexpected_error_occurred
                ErrorStatus.EMPTY -> R.string.no_results_found
                ErrorStatus.PARSING_ERROR -> R.string.error_parsing_data
                ErrorStatus.UNAUTHORIZED -> R.drawable.no_result
            },
            descriptionId = when (errorStatus) {
                ErrorStatus.NO_INTERNET -> R.string.internet_is_not_available_description
                ErrorStatus.NETWORK_ERROR -> R.string.internet_is_not_available_description
                ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred_description
                null -> R.string.an_unexpected_error_occurred_description
                ErrorStatus.UNAUTHORIZED -> R.string.unauthorized_access
                ErrorStatus.EMPTY -> R.string.no_results_found
                ErrorStatus.PARSING_ERROR -> R.string.error_parsing_data
            }
        )
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