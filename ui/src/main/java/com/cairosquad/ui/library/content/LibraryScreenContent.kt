package com.cairosquad.ui.library.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.component.EmptyListContainer
import com.cairosquad.ui.library.component.ListContainer
import com.cairosquad.ui.library.component.SectionHeader
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.library.LibraryInteractionListener
import com.cairosquad.viewmodel.library.LibraryScreenState

@Composable
fun LibraryScreenContent(
    screenState: LibraryScreenState,
    listener: LibraryInteractionListener
) {

    Column {
        AppBar(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .background(Theme.color.surfaces.surface),
            title = stringResource(com.cairosquad.ui.R.string.library)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surfaces.surface)
        ) {
            when (screenState.screenStatus) {
                LibraryScreenState.SectionStatus.LOADING -> {}
                LibraryScreenState.SectionStatus.SUCCESS -> {
                    if (screenState.isUserAuthed) {
                        item { ListsSection(listener, screenState) }
                        item { FavoriteSection(screenState, listener) }
                        item { HistorySection(screenState, listener) }
                    }
                }

                LibraryScreenState.SectionStatus.ERROR -> {
                    item {
                        LibraryFailContent(
                            screenState.errorStatus,
                            listener = listener
                        )
                    }
                }
            }
        }
    }
    when (screenState.screenStatus) {
        LibraryScreenState.SectionStatus.LOADING -> {}
        LibraryScreenState.SectionStatus.SUCCESS -> {
            if (!screenState.isUserAuthed) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    StateMessage(
                        imageDrawable = R.drawable.logo,
                        titleId = com.cairosquad.ui.R.string.log_in_to_unlock_your_personal_library,
                        descriptionId = com.cairosquad.ui.R.string.access_your_watch_history_favorites_and_watchlist_all_in_one_place,
                        width = 80.dp,
                        height = 88.dp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        text = stringResource(com.cairosquad.ui.R.string.login),
                        onClick = listener::onLoginClicked
                    )
                }
            }
        }

        LibraryScreenState.SectionStatus.ERROR -> {}
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
                                modifier = Modifier.width(158.dp),
                                listName = it.name,
                                numberOfItems = it.mediaCount,
                                onListClicked = { listener.onListClicked(it.id, it.name) }
                            )
                        }
                        items(screenState.seriesLists) {
                            ListContainer(
                                modifier = Modifier.width(158.dp),
                                listName = it.name,
                                numberOfItems = it.mediaCount,
                                onListClicked = { listener.onListClicked(it.id, it.name) }
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
                    sectionTitle = stringResource(com.cairosquad.ui.R.string.history),
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
                        sectionTitle = stringResource(com.cairosquad.ui.R.string.history),
                        sectionDescription = stringResource(com.cairosquad.ui.R.string.this_list_has_empty),
                        sectionIcon = ImageVector.vectorResource(R.drawable.recent),
                        onSectionClick = listener::onHistoryViewAllClick
                    )
                } else {
                    SectionHeader(
                        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                        sectionTitle = stringResource(com.cairosquad.ui.R.string.history),
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