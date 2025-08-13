package com.cairosquad.ui.rated

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.rated.MyRatingsEffect
import com.cairosquad.viewmodel.rated.MyRatingsInteractionListener
import com.cairosquad.viewmodel.rated.MyRatingsScreenState
import com.cairosquad.viewmodel.rated.MyRatingsViewModel
import com.cairosquad.viewmodel.rated.RatedItemUiState

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
    MyRatingsScreenContent(
        modifier = modifier,
        state = state,
        interactionListener = viewModel
    )
}

@Composable
fun MyRatingsScreenContent(
    modifier: Modifier,
    state: MyRatingsScreenState,
    interactionListener: MyRatingsInteractionListener
) {
    val ratedItems = state.ratedItems.collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        com.cairosquad.design_system.basic_component.AppBar(
            onBackButtonClicked = {
                interactionListener.onBackPressed()
            },
            title = stringResource(R.string.my_ratings),
            modifier = Modifier.statusBarsPadding()
        )
        RatedItemsList(
            ratedItems = ratedItems,
            onItemClick = { itemId, isMovie ->
                interactionListener.onItemClicked(itemId, isMovie)
            }
        )
    }
}

@Composable
fun RatedItemsList(
    ratedItems: LazyPagingItems<RatedItemUiState>,
    onItemClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
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
        }

        else {
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
                            item = item,
                            onItemClick = onItemClick,
                            modifier = Modifier.fillMaxWidth().animateItem()
                        )
                    }
                }

            }
        }
    }
}