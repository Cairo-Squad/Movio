package com.cairosquad.ui.see_all_screen

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
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
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
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun SeeAllScreen(
    contentType: MediaContentType,
    mediaType: MediaType,
    viewModel: SeeAllViewModel = koinViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    val navController = LocalNavController.current
    val media = state.mediaList.collectAsLazyPagingItems()
    LaunchedEffect(contentType, mediaType) {
        viewModel.loadData(
            contentType = contentType, mediaType = mediaType
        )
    }
    LaunchedEffect(media.loadState) {
        when (val state = media.loadState.refresh) {
            is androidx.paging.LoadState.Loading -> {
                viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.LOADING)
            }

            is androidx.paging.LoadState.NotLoading -> {
                if (media.itemCount == 0) {
                    viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.Empty)
                } else {
                    viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.SUCCESS)
                }
            }

            is androidx.paging.LoadState.Error -> {
                viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.FAILED)
                val errorStatus = viewModel.handleHomeException(state.error)
                viewModel.updateErrorStatus(errorStatus)
            }
        }
    }
    ObserveAsEffect(viewModel.effect) { effect ->
        effectDiscoverHandler(
            effect, navController
        )
    }

    RefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onRefresh(state.selectedGenreIndex) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.background(Theme.color.surfaces.surface)) {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .align(Alignment.TopEnd)
                    .size(230.dp)
                    .blur(263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Theme.color.surfaces.onSurfaceAt5, shape = CircleShape)
            )
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
                    categories =state.genres.map { genre ->
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
                        listener = viewModel,
                        media = media
                    )
                } else {
                    TrendingContentList(
                        modifier = Modifier
                            .padding(top = 24.dp, bottom = 16.dp)
                            .padding(horizontal = 16.dp),
                        listener = viewModel,
                        state = state,
                        media = media
                    )
                }
            }
        }
    }
}

@Composable
private fun SeeAllMediaItems(
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    state: SeeAllScreenState,
    listener: SeeAllInteractionsListener,
    modifier: Modifier = Modifier
) {

    when {

        state.screenStatus == SeeAllScreenState.ScreenStatus.FAILED -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable = R.drawable.no_internet,
                    titleId = R.string.no_internet_connection,
                    descriptionId = R.string.internet_is_not_available_description
                )
            }
        }

        state.screenStatus == SeeAllScreenState.ScreenStatus.LOADING -> {
            SeeAllLoadingScreen(modifier)
        }

        state.screenStatus == SeeAllScreenState.ScreenStatus.Empty ->{
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable = R.drawable.no_result,
                    titleId = R.string.no_results_found,
                    descriptionId = R.string.no_results_found_description
                )
            }
        }

        else -> {
            MediaGridContent(media, listener, modifier)
        }
    }
}

@Composable
private fun TrendingContentList(
    listener: SeeAllInteractionsListener,
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    state: SeeAllScreenState,
    modifier: Modifier = Modifier
) {
    when {

        state.screenStatus == SeeAllScreenState.ScreenStatus.FAILED -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable = R.drawable.no_internet,
                    titleId = R.string.no_internet_connection,
                    descriptionId = R.string.internet_is_not_available_description
                )
            }
        }

        state.screenStatus == SeeAllScreenState.ScreenStatus.LOADING -> {
            SeeAllLoadingScreen(modifier)
        }

        state.screenStatus == SeeAllScreenState.ScreenStatus.Empty -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable = R.drawable.no_result,
                    titleId = R.string.no_results_found,
                    descriptionId = R.string.no_results_found_description
                )
            }
        }

        else -> {
            MediaListContent(media, listener, modifier)
        }
    }
}

@Composable
fun MediaGridContent(
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    listener: SeeAllInteractionsListener,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(media.itemCount) { index ->
            media[index]?.let { item ->
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

@Composable
fun MediaListContent(
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    listener: SeeAllInteractionsListener,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(media.itemCount) { index ->
            media[index]?.let { mediaItem ->
                TrendingMovieCard(
                    modifier = Modifier.clickable {
                        listener.onClickMedia(mediaItem.id, mediaItem.isMovie)
                    },
                    imgUrl = mediaItem.posterPath,
                    rating = String.format(
                        Locale.getDefault(), "%.1f", mediaItem.rating
                    ),
                    movieTitle = mediaItem.title,
                    movieCategory = mediaItem.genres.firstOrNull()?.name.orEmpty()
                )
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