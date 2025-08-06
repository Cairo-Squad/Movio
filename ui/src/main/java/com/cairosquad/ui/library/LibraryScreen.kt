package com.cairosquad.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.component.EmptyListContainer
import com.cairosquad.ui.library.component.ListContainer
import com.cairosquad.ui.library.component.SectionHeader
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.library.LibraryScreenState
import com.cairosquad.viewmodel.library.LibraryViewModel

@Composable
fun LibraryScreen() {
	val viewModel: LibraryViewModel = hiltViewModel()
	val screenState by viewModel.screenState.collectAsStateWithLifecycle()

	LaunchedEffect(Unit) {
		viewModel.loadScreenState()
	}

	LibraryScreenContent(
		screenState = screenState
	)
}

@Composable
private fun LibraryScreenContent(screenState: LibraryScreenState) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.statusBars)
			.background(Theme.color.surfaces.surface)
	) {
		stickyHeader {
			AppBar(title = stringResource(com.cairosquad.ui.R.string.library))
		}
		item {
			SectionHeader(
				modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
				sectionTitle = stringResource(com.cairosquad.ui.R.string.watchlist),
				sectionIcon = ImageVector.vectorResource(R.drawable.ic_list),
				onSectionClick = {}
			)
		}
		item {
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
						if (screenState.movieLists.isEmpty() && screenState.seriesLists.isEmpty())
						item {
							EmptyListContainer { }
						} else {
							items(screenState.movieLists) {
								ListContainer(
                                    listName = it.name,
                                    numberOfItems = it.mediaCount,
                                    onListClicked = {}
                                )
							}
							items(screenState.seriesLists) {
								ListContainer(
									listName = it.name,
									numberOfItems = it.mediaCount,
									onListClicked = {}
								)
							}
						}
					}
                    LibraryScreenState.SectionStatus.ERROR -> {}
                }
			}
		}
		item {
			when (screenState.favoritesSectionState) {
				LibraryScreenState.SectionStatus.LOADING -> {
					SectionHeader(
						modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
						sectionTitle = stringResource(com.cairosquad.ui.R.string.favorite),
						sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
						onSectionClick = {}
					)
				}
				LibraryScreenState.SectionStatus.SUCCESS -> {
					if (screenState.favoriteMovies.isEmpty() && screenState.favoriteSeries.isEmpty()) {
						SectionHeader(
							modifier = Modifier.padding(top = 24.dp),
							sectionTitle = stringResource(com.cairosquad.ui.R.string.favorite),
							sectionDescription = stringResource(com.cairosquad.ui.R.string.this_list_has_empty),
							sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
							onSectionClick = {}
						)
					} else {
						SectionHeader(
							modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
							sectionTitle = stringResource(com.cairosquad.ui.R.string.favorite),
							sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
							onSectionClick = {}
						)

					}
				}
				LibraryScreenState.SectionStatus.ERROR -> {}
			}
		}
		item {
			when (screenState.favoritesSectionState) {
				LibraryScreenState.SectionStatus.LOADING -> {
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
					if (screenState.favoriteMovies.isNotEmpty() || screenState.favoriteSeries.isNotEmpty()) {
						LazyRow(
							horizontalArrangement = Arrangement.spacedBy(12.dp),
							contentPadding = PaddingValues(horizontal = 16.dp)
						) {
							items(screenState.favoriteMovies) {
								MovieCard(
									modifier = Modifier
										.width(124.dp),
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
										.width(124.dp),
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
		item {
			when (screenState.historySectionState) {
                LibraryScreenState.SectionStatus.LOADING -> {
					SectionHeader(
						modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
						sectionTitle = "History",
						sectionIcon = ImageVector.vectorResource(R.drawable.recent),
						onSectionClick = {}
					)
				}
                LibraryScreenState.SectionStatus.SUCCESS -> {
					if (screenState.historyMovies.isEmpty() && screenState.historySeries.isEmpty()) {
						SectionHeader(
							modifier = Modifier.padding(top = 24.dp),
							sectionTitle = "History",
							sectionDescription = stringResource(com.cairosquad.ui.R.string.this_list_has_empty),
							sectionIcon = ImageVector.vectorResource(R.drawable.recent),
							onSectionClick = {}
						)
					} else {
						SectionHeader(
							modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
							sectionTitle = "History",
							sectionIcon = ImageVector.vectorResource(R.drawable.recent),
							onSectionClick = {}
						)

					}
				}
                LibraryScreenState.SectionStatus.ERROR -> {}
            }
		}
		item {
			when (screenState.historySectionState) {
                LibraryScreenState.SectionStatus.LOADING -> {
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
					if (screenState.historyMovies.isNotEmpty() || screenState.historySeries.isNotEmpty()) {
						LazyRow(
							horizontalArrangement = Arrangement.spacedBy(12.dp),
							contentPadding = PaddingValues(horizontal = 16.dp)
						) {
							items(screenState.historyMovies) {
								MovieCard(
									modifier = Modifier
										.width(124.dp),
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
										.width(124.dp),
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
}


@Preview
@Composable
private fun LibraryScreenPreview() {
	MovioTheme(isDarkTheme = true) {
		LibraryScreenContent(LibraryScreenState(
			favoritesSectionState = LibraryScreenState.SectionStatus.SUCCESS
		))
	}
}