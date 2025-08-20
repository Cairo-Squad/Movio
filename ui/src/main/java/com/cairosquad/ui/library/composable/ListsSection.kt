package com.cairosquad.ui.library.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.library.LibraryInteractionListener
import com.cairosquad.viewmodel.library.LibraryScreenState

@Composable
fun ListsSection(
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
                                onListClicked = { listener.onListClick(it.id, it.name) }
                            )
                        }
                        items(screenState.seriesLists) {
                            ListContainer(
                                modifier = Modifier.width(158.dp),
                                listName = it.name,
                                numberOfItems = it.mediaCount,
                                onListClicked = { listener.onListClick(it.id, it.name) }
                            )
                        }
                    }
                }

                LibraryScreenState.SectionStatus.ERROR -> {}
            }
        }
    }
}