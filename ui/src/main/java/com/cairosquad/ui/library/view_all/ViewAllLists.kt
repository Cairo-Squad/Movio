package com.cairosquad.ui.library.view_all

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.component.ListContainer
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.bottom_sheet.CreateListBottomSheet
import com.cairosquad.ui.navigation.ListRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.getSnackBarIcon
import com.cairosquad.viewmodel.library.view_all_lists.ViewAllListsEffect
import com.cairosquad.viewmodel.library.view_all_lists.ViewAllListsInteractionListener
import com.cairosquad.viewmodel.library.view_all_lists.ViewAllListsScreenState
import com.cairosquad.viewmodel.library.view_all_lists.ViewAllListsViewModel

@Composable
fun ViewAllLists(
    viewModel: ViewAllListsViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ViewAllListsEffect.OnMovieListClicked -> {
                navController.navigate(ListRoute(effect.listId, effect.listName))
            }

            ViewAllListsEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ViewAllListsEffect.OnSeriesListClicked -> {
                navController.navigate(ListRoute(effect.listId, effect.listName))
            }
        }
    }

    ViewAllListsContent(
        screenState = uiState,
        listener = viewModel
    )
}

@Composable
private fun ViewAllListsContent(
    screenState: ViewAllListsScreenState,
    listener: ViewAllListsInteractionListener,
    modifier: Modifier = Modifier
) {
    val moviesLists = screenState.movieLists
    val seriesLists = screenState.seriesLists

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
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
        Column {
            AppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = stringResource(com.cairosquad.ui.R.string.watchlist),
                onBackButtonClicked = listener::onNavigateBack,
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            when (screenState.screenStatus) {
                ViewAllListsScreenState.SectionStatus.LOADING -> {

                }

                ViewAllListsScreenState.SectionStatus.SUCCESS -> {
                    if (moviesLists.isNotEmpty() || seriesLists.isNotEmpty()){
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 158.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal =  16.dp, vertical = 12.dp)
                        ) {
                            items(
                                moviesLists.size
                            ) { index ->
                                moviesLists[index]?.let { movieList ->
                                    ListContainer(
                                        listName = movieList.name,
                                        numberOfItems = movieList.mediaCount,
                                        onListClicked = {
                                            listener.onMovieListClick(
                                                movieList.id,
                                                movieList.name
                                            )
                                        }
                                    )
                                }
                            }
                            items(
                                seriesLists.size
                            ) { index ->
                                seriesLists[index]?.let { seriesList ->

                                    ListContainer(
                                        listName = seriesList.name,
                                        numberOfItems = seriesList.mediaCount,
                                        onListClicked = {
                                            listener.onSeriesListClick(
                                                seriesList.id,
                                                seriesList.name
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            StateMessage(
                                imageDrawable =
                                    if (Theme.isDark) R.drawable.favorite_list_empty_dark
                                    else R.drawable.favorite_list_empty,
                                title = stringResource(com.cairosquad.ui.R.string.nothing_here_yet),
                                description = stringResource(com.cairosquad.ui.R.string.add_movies_and_tv_shows_to_build_your_personal_watchlist_the_perfect_binge_starts_here)
                            )
                        }
                    }

                }

                ViewAllListsScreenState.SectionStatus.ERROR -> {}
            }
        }
        when (screenState.screenStatus) {
            ViewAllListsScreenState.SectionStatus.LOADING -> {}
            ViewAllListsScreenState.SectionStatus.SUCCESS -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(vertical = 32.dp, horizontal = 24.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Theme.color.brand.primary)
                        .clickable(onClick = {
                            listener.onAddListClick()
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = "Add Icon",
                        tint = Theme.color.brand.onPrimary
                    )
                }
            }

            ViewAllListsScreenState.SectionStatus.ERROR -> {}
        }

        CreateListBottomSheet(
            isVisible = screenState.showCreateListBottomSheet,
            onDismiss = listener::onDismissCreateListBottomSheet,
            value = screenState.listName,
            onValueChange = listener::onListValueChange,
            onSubmit = { listener.onSubmitCreateListClick() },
            isMovie = true,
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(16.dp),
            visible = screenState.showSnackBar,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> 2 * fullHeight },
                animationSpec = tween(durationMillis = 600)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> 2 * fullHeight },
                animationSpec = tween(durationMillis = 600)
            )
        ) {
            SnackBar(
                imageVector = getSnackBarIcon(screenState.isProcessSuccess),
                message = stringResource(screenState.snackMessageId),
                action = {}
            )
        }
    }
}