package com.cairosquad.ui.home.composable

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
import com.cairosquad.ui.navigation.DiscoverRoute
import com.cairosquad.viewmodel.home.listner.DiscoverInteractionsListener
import com.cairosquad.viewmodel.home.state.DiscoverScreenState
import com.cairosquad.viewmodel.home.model.DiscoverType
import com.cairosquad.viewmodel.home.model.MediaType

@Composable
fun DiscoverMediaItems(
    state: DiscoverScreenState,
    route: DiscoverRoute,
    listener: DiscoverInteractionsListener,
    modifier: Modifier = Modifier
) {
    val movieItems = when (route.type) {
        DiscoverType.TRENDING -> state.trendingMovies
        DiscoverType.TOP_RATING -> state.topRatedMovies
        DiscoverType.MORE_RECOMMENDED -> state.moreRecommendedMovies
        DiscoverType.FREE_TO_WATCH -> state.freeToWatchMovies
        DiscoverType.UPCOMING -> state.upcomingMovies
    }

    val seriesItems = when (route.type) {
        DiscoverType.TRENDING -> state.trendingSeries
        DiscoverType.TOP_RATING -> state.topRatedSeries
        DiscoverType.MORE_RECOMMENDED -> state.moreRecommendedSeries
        DiscoverType.FREE_TO_WATCH -> state.freeToWatchSeries
        DiscoverType.UPCOMING -> state.upcomingSeries
    }
    val items = when (route.mediaType) {
        MediaType.Movies -> movieItems
        MediaType.Series -> seriesItems
        MediaType.All ->movieItems
    }
    AnimatedVisibility(visible = items.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
    }

    AnimatedVisibility(visible = items.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            items(items) { item ->
                when (item) {
                    is DiscoverScreenState.MovieUiState -> {
                        MovieCard(
                            modifier = Modifier.clickable { listener.onClickMovie(item.id) },
                            title = item.title,
                            vote = item.rating,
                            imgUrl = item.posterPath,
                            width = null,
                            aspectRatio = 0.743f
                        )
                    }
                    is DiscoverScreenState.SeriesUiState -> {
                        MovieCard(
                            modifier = Modifier.clickable { listener.onClickSeries(item.id) },
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
