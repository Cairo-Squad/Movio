package com.cairosquad.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.component.EmptyListContainer
import com.cairosquad.ui.library.component.ListContainer
import com.cairosquad.ui.library.component.SectionHeader
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.navigation.ListRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.navigation.ViewAllFavoritesRoute
import com.cairosquad.ui.navigation.ViewAllHistoryRoute
import com.cairosquad.ui.navigation.ViewAllListsRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.LibraryEffect
import com.cairosquad.viewmodel.library.LibraryInteractionListener
import com.cairosquad.viewmodel.library.LibraryScreenState
import com.cairosquad.viewmodel.library.LibraryViewModel

@Composable
fun LibraryScreen() {
    val viewModel: LibraryViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.loadScreenState()
    }

    ObserveAsEffect(viewModel.effect) {
        when (it) {
            LibraryEffect.NavigateToFavorites -> {
                navController.navigate(ViewAllFavoritesRoute)
            }

            LibraryEffect.NavigateToHistory -> {
                navController.navigate(ViewAllHistoryRoute)
            }

            LibraryEffect.NavigateToLists -> {
                navController.navigate(ViewAllListsRoute)
            }

            is LibraryEffect.NavigateToListDetails -> {
                navController.navigate(ListRoute(it.listId))
            }

            is LibraryEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }

            is LibraryEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }
        }
    }

    LibraryScreenContent(
        screenState = screenState,
        listener = viewModel
    )
}

@Composable
private fun LibraryScreenContent(
    screenState: LibraryScreenState,
    listener: LibraryInteractionListener
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(Theme.color.surfaces.surface)
    ) {
        stickyHeader {
            AppBar(
                modifier = Modifier.background(Theme.color.surfaces.surface),
                title = stringResource(com.cairosquad.ui.R.string.library)
            )
        }
        item { ListsSection(listener, screenState) }
        item { FavoriteSection(screenState, listener) }
        item { HistorySection(screenState, listener) }
    }
}

@Composable
private fun ListsSection(
    listener: LibraryInteractionListener,
    screenState: LibraryScreenState
) {
    Column {
        SectionHeader(
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
            sectionTitle = stringResource(com.cairosquad.ui.R.string.watchlist),
            sectionIcon = ImageVector.vectorResource(R.drawable.ic_list),
            onSectionClick = listener::onListsViewAllClick
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            when (screenState.listsSectionState) {
                LibraryScreenState.SectionStatus.LOADING -> {
                    items(5) {
                        LoadingMovieImage(
                            modifier = Modifier.size(width = 158.dp, height = 128.dp)
                        )
                    }
                }

                LibraryScreenState.SectionStatus.SUCCESS -> {
                    if (screenState.movieLists.isEmpty() && screenState.seriesLists.isEmpty()) {
                        item { EmptyListContainer { } }
                    } else {
                        items(screenState.movieLists) {
                            ListContainer(
                                listName = it.name,
                                numberOfItems = it.mediaCount,
                                onListClicked = { listener.onListClicked(it.id) }
                            )
                        }
                        items(screenState.seriesLists) {
                            ListContainer(
                                listName = it.name,
                                numberOfItems = it.mediaCount,
                                onListClicked = { listener.onListClicked(it.id) }
                            )
                        }
                    }
                }

                LibraryScreenState.SectionStatus.ERROR -> {}
            }
        }
    }
}

@Composable
private fun FavoriteSection(
    screenState: LibraryScreenState,
    listener: LibraryInteractionListener
) {
    Column {
        when (screenState.favoritesSectionState) {
            LibraryScreenState.SectionStatus.LOADING -> {
                SectionHeader(
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                    sectionTitle = stringResource(com.cairosquad.ui.R.string.favorite),
                    sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
                    onSectionClick = listener::onFavoritesViewAllClick
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(10) {
                        LoadingMovieCard(
                            modifier = Modifier.width(124.dp),
                            height = 160.dp,
                        )
                    }
                }
            }

            LibraryScreenState.SectionStatus.SUCCESS -> {
                if (screenState.favoriteMovies.isEmpty() && screenState.favoriteSeries.isEmpty()) {
                    SectionHeader(
                        modifier = Modifier.padding(top = 24.dp),
                        sectionTitle = stringResource(com.cairosquad.ui.R.string.favorite),
                        sectionDescription = stringResource(com.cairosquad.ui.R.string.this_list_has_empty),
                        sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
                        onSectionClick = listener::onFavoritesViewAllClick
                    )
                } else {
                    SectionHeader(
                        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                        sectionTitle = stringResource(com.cairosquad.ui.R.string.favorite),
                        sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
                        onSectionClick = listener::onFavoritesViewAllClick
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(screenState.favoriteMovies) {
                            MovieCard(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .width(124.dp)
                                    .clickable(onClick = { listener.onMovieClicked(it.id) }),
                                title = it.title,
                                vote = it.rating,
                                imgUrl = it.posterPath,
                                width = 124.dp,
                                aspectRatio = 0.775f
                            )
                        }
                        items(screenState.favoriteSeries) {
                            MovieCard(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .width(124.dp)
                                    .clickable(onClick = { listener.onSeriesClicked(it.id) }),
                                title = it.title,
                                vote = it.rating,
                                imgUrl = it.posterPath,
                                width = 124.dp,
                                aspectRatio = 0.775f
                            )
                        }
                    }
                }
            }

            LibraryScreenState.SectionStatus.ERROR -> {}
        }
    }
}

@Composable
private fun HistorySection(
    screenState: LibraryScreenState,
    listener: LibraryInteractionListener
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (screenState.historySectionState) {
            LibraryScreenState.SectionStatus.LOADING -> {
                SectionHeader(
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                    sectionTitle = "History",
                    sectionIcon = ImageVector.vectorResource(R.drawable.recent),
                    onSectionClick = listener::onHistoryViewAllClick
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(10) {
                        LoadingMovieCard(
                            modifier = Modifier.width(124.dp),
                            height = 160.dp,
                        )
                    }
                }
            }

            LibraryScreenState.SectionStatus.SUCCESS -> {
                if (screenState.historyMovies.isEmpty() && screenState.historySeries.isEmpty()) {
                    SectionHeader(
                        modifier = Modifier.padding(top = 24.dp),
                        sectionTitle = "History",
                        sectionDescription = stringResource(com.cairosquad.ui.R.string.this_list_has_empty),
                        sectionIcon = ImageVector.vectorResource(R.drawable.recent),
                        onSectionClick = listener::onHistoryViewAllClick
                    )
                } else {
                    SectionHeader(
                        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                        sectionTitle = "History",
                        sectionIcon = ImageVector.vectorResource(R.drawable.recent),
                        onSectionClick = listener::onHistoryViewAllClick
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(screenState.historyMovies) {
                            MovieCard(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .width(124.dp)
                                    .clickable(onClick = { listener.onMovieClicked(it.id) }),
                                title = it.title,
                                vote = it.rating,
                                imgUrl = it.posterPath,
                                width = 124.dp,
                                aspectRatio = 0.775f
                            )
                        }
                        items(screenState.historySeries) {
                            MovieCard(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .width(124.dp)
                                    .clickable(onClick = { listener.onSeriesClicked(it.id) }),
                                title = it.title,
                                vote = it.rating,
                                imgUrl = it.posterPath,
                                width = 124.dp,
                                aspectRatio = 0.775f
                            )
                        }
                    }
                }
            }

            LibraryScreenState.SectionStatus.ERROR -> {}
        }
    }
}