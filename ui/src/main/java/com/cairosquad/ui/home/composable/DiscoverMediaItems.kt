package com.cairosquad.ui.home.composable

import DiscoverContentStrategy
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState

@Composable
fun DiscoverMediaItems(
    discoverContentStrategy: DiscoverContentStrategy,
    state: HomeScreenState,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier
) {
    val items =   discoverContentStrategy.getItems(state).filterIsInstance<HomeScreenState.MediaUiState>()
    AnimatedVisibility(
        visible = items.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
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

    AnimatedVisibility(
        visible = items.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(items) { item ->
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