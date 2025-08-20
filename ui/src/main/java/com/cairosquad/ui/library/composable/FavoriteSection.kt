package com.cairosquad.ui.library.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.library.LibraryInteractionListener
import com.cairosquad.viewmodel.library.LibraryScreenState

@Composable
fun FavoriteSection(
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
                                    .clickable(onClick = { listener.onMovieClick(it.id) }),
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
                                    .clickable(onClick = { listener.onSeriesClick(it.id) }),
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