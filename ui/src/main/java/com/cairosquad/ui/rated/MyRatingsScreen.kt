package com.cairosquad.ui.rated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.getSnackBarIcon
import com.cairosquad.viewmodel.rated.MyRatingsEffect
import com.cairosquad.viewmodel.rated.MyRatingsInteractionListener
import com.cairosquad.viewmodel.rated.MyRatingsScreenState
import com.cairosquad.viewmodel.rated.MyRatingsViewModel

@Composable
fun MyRatingsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyRatingsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            MyRatingsEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MyRatingsEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }

            is MyRatingsEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }
        }
    }
    Box {
        MyRatingsScreenContent(
            modifier = modifier,
            state = state,
            listener = viewModel
        )

        UndoRatingSnackBar(state, viewModel)
    }
}

@Composable
private fun BoxScope.UndoRatingSnackBar(
    state: MyRatingsScreenState,
    viewModel: MyRatingsViewModel
) {
    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp),
        visible = state.showSnackBar,
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
            imageVector = getSnackBarIcon(state.isProcessSuccess),
            message = stringResource(state.snackMessageId),
            action = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = viewModel::onUndoClick)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.undo),
                        style = Theme.textStyle.label.smallRegular14,
                        color = Theme.color.brand.primary
                    )
                }
            }
        )
    }
}


@Composable
fun MyRatingsScreenContent(
    modifier: Modifier,
    state: MyRatingsScreenState,
    listener: MyRatingsInteractionListener
) {
    val ratedItems = state.ratedItems.collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        AppBar(
            onBackButtonClicked = {
                listener.onBackPressed()
            },
            title = stringResource(R.string.my_ratings),
            modifier = Modifier.statusBarsPadding()
        )
        RatedItemsList(
            ratedItems = ratedItems,
            onItemClick = { itemId, isMovie ->
                if (isMovie) {
                    listener.onMovieClick(itemId)
                } else {
                    listener.onSeriesClick(itemId)
                }
            },
            onMovieDelete = listener::onMovieDelete,
            onSeriesDelete = listener::onSeriesDelete
        )
    }
}

@Composable
fun RatedItemsList(
    ratedItems: LazyPagingItems<MyRatingsScreenState.RatedItemUiState>,
    onItemClick: (Long, Boolean) -> Unit,
    onMovieDelete: (Long, Double) -> Unit,
    onSeriesDelete: (Long, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .navigationBarsPadding()
    ) {
        if (ratedItems.loadState.refresh is LoadState.Loading) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(10) {
                    LoadingMovieCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }
        }
        else if (ratedItems.itemCount == 0 && ratedItems.loadState.refresh !is LoadState.Loading) {
            StateMessage(
                imageDrawable =
                    if (Theme.isDark) com.cairosquad.design_system.R.drawable.favorite_list_empty_dark
                    else com.cairosquad.design_system.R.drawable.favorite_list_empty,
                titleId = R.string.no_ratings_yet,
                descriptionId = R.string.no_ratings_found_description,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                width = 180.dp,
                height = 150.dp,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    ratedItems.itemCount,
                    key = { index -> ratedItems[index]?.id ?: index }
                ) { index ->
                    ratedItems[index]?.let { item ->
                        RatedItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            item = item,
                            onItemClick = onItemClick,
                            onMovieDelete = onMovieDelete,
                            onSeriesDelete = onSeriesDelete
                        )
                    }
                }
            }
        }
    }
}