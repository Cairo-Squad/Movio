package com.cairosquad.ui.see_all_screen

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.see_all.SeeAllEffect
import com.cairosquad.viewmodel.see_all.SeeAllInteractionsListener
import com.cairosquad.viewmodel.see_all.SeeAllScreenState
import com.cairosquad.viewmodel.see_all.SeeAllViewModel
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import java.util.Locale

@Composable
fun SeeAllScreen(
	contentType: MediaContentType,
	mediaType: MediaType,
	viewModel: SeeAllViewModel = hiltViewModel()
) {
	val state by viewModel.screenState.collectAsState()

	val navController = LocalNavController.current

	LaunchedEffect(contentType, mediaType) {
		viewModel.loadData(
			contentType = contentType,
			mediaType = mediaType
		)
	}
	ObserveAsEffect(viewModel.effect) { effect ->
		effectDiscoverHandler(
			effect, navController
		)
	}

	Box(modifier = Modifier.background(Theme.color.surfaces.surface)) {
		Box(
			modifier = Modifier
				.windowInsetsPadding(WindowInsets.statusBars)
				.size(230.dp)
				.align(Alignment.TopEnd)
				.then(
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
						Modifier.dropShadow(
							shape = CircleShape,
							color = Theme.color.surfaces.onSurfaceAt5,
							blur = 264.dp,
							offsetX = 0.dp,
							offsetY = 0.dp,
							alpha = 0.10f
						)
					} else {
						Modifier
							.blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
							.background(
								color = Theme.color.surfaces.onSurfaceAt5,
								shape = CircleShape
							)
					}
				)
		)
	}
	Column(
		modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.navigationBars)
	) {
		AppBar(
			modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
			title = stringResource(contentType.titleId),
			onBackButtonClicked = {
				navController.popBackStack()
			},
			onShareButtonClicked = null,
			onFavoriteButtonClicked = null
		)

		CategoriesChips(
			modifier = Modifier.padding(top = 16.dp),
			categories = state.genres.map { genre ->
				if (genre.id == null) {
					stringResource(com.cairosquad.ui.R.string.all)
				} else {
					genre.name
				}
			},
			selectedChipIndex = state.selectedGenreIndex,
			onChipSelected = { index ->
				viewModel.onGenreSelected(index)
			})

		if (contentType != MediaContentType.TRENDING) {
			SeeAllMediaItems(
				modifier = Modifier
					.padding(top = 24.dp, bottom = 16.dp)
					.padding(horizontal = 16.dp),
				state = state,
				listener = viewModel
			)
		} else {
			TrendingContentList(
				modifier = Modifier
					.padding(top = 24.dp, bottom = 16.dp)
					.padding(horizontal = 16.dp),
				listener = viewModel,
				state = state,
			)
		}
	}
}

@Composable
private fun SeeAllMediaItems(
	state: SeeAllScreenState,
	listener: SeeAllInteractionsListener,
	modifier: Modifier = Modifier
) {
	when {

		state.screenStatus == SeeAllScreenState.ScreenStatus.FAILED -> {
			AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
				Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					StateMessage(
						imageDrawable = R.drawable.no_internet,
						titleId = R.string.no_internet_connection,
						descriptionId = R.string.internet_is_not_available_description
					)
				}
			}
		}

		state.isLoading -> {
			SeeAllLoadingScreen(modifier)
		}

		! state.isLoading && state.mediaList.isEmpty() -> {
			AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
				Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					StateMessage(
						imageDrawable = R.drawable.no_result,
						titleId = R.string.no_results_found,
						descriptionId = R.string.no_results_found_description
					)
				}
			}
		}

		else -> {
			AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
				LazyVerticalGrid(
					modifier = modifier.fillMaxSize(),
					columns = GridCells.Adaptive(minSize = 101.33.dp),
					horizontalArrangement = Arrangement.spacedBy(12.dp),
					verticalArrangement = Arrangement.spacedBy(12.dp),
					contentPadding = PaddingValues(bottom = 16.dp)
				) {
					items(state.mediaList) { item ->
						MovieCard(
							modifier = Modifier.clickable {
								listener.onClickMedia(item.id, item.isMovie)
							},
							title = item.title,
							vote = item.rating,
							imgUrl = item.posterPath,
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
private fun TrendingContentList(
	listener: SeeAllInteractionsListener,
	state: SeeAllScreenState,
	modifier: Modifier = Modifier
) {
	when {

		state.screenStatus == SeeAllScreenState.ScreenStatus.FAILED -> {
			AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
				Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					StateMessage(
						imageDrawable = R.drawable.no_internet,
						titleId = R.string.no_internet_connection,
						descriptionId = R.string.internet_is_not_available_description
					)
				}
			}
		}

		state.isLoading -> {
			SeeAllLoadingScreen(modifier)
		}

		! state.isLoading && state.mediaList.isEmpty() -> {
			AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
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
		}

		else -> {
			AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
				LazyColumn(
					modifier = modifier.fillMaxSize(),
					verticalArrangement = Arrangement.spacedBy(12.dp),
					contentPadding = PaddingValues(bottom = 16.dp)
				) {
					items(state.mediaList) { mediaItem ->
						TrendingMovieCard(
							modifier = Modifier.clickable {
								listener.onClickMedia(mediaItem.id, mediaItem.isMovie)
							},
							imgUrl = mediaItem.posterPath,
							rating = String.format(Locale.getDefault(), "%.1f", mediaItem.rating),
							movieTitle = mediaItem.title,
							movieCategory = mediaItem.genres.firstOrNull()?.name.orEmpty()
						)
					}
				}
			}
		}
	}
}

private fun effectDiscoverHandler(
	effect: SeeAllEffect, navController: NavController
) {

	when (effect) {
		SeeAllEffect.NavigateBack -> {
			navController.popBackStack()
		}

		is SeeAllEffect.NavigateMediaDetails -> {
			if (effect.isMovie) {
				navController.navigate(MovieRoute(effect.mediaId))
			} else {
				navController.navigate(SeriesRoute(effect.mediaId))
			}
		}
	}
}