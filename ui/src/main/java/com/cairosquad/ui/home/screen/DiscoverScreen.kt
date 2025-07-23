package com.cairosquad.ui.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.home.composable.DiscoverMediaItems
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.ui.navigation.DiscoverRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.state.DiscoverScreenState
import com.cairosquad.viewmodel.home.DiscoverViewModel
import com.cairosquad.viewmodel.home.model.DiscoverType
import com.cairosquad.viewmodel.home.model.MediaType
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun DiscoverScreen(route: DiscoverRoute, modifier: Modifier = Modifier) {
    val viewModel: DiscoverViewModel = koinViewModel()
    val state by viewModel.screenState.collectAsState()
    val navController = LocalNavController.current

    LaunchedEffect(route) {
        viewModel.loadDiscoverContent(route.type, route.mediaType)
    }
    Box(modifier = Modifier.background(Theme.color.surfaces.surface)) {
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopEnd)
                .size(230.dp)
                .blur(263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(Color(0x33734EF8), shape = CircleShape)
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            AppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = route.type.name.replace("_", " "),
                onBackButtonClicked = {
                    navController.popBackStack()
                },
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )

            CategoriesChips(
                modifier = Modifier.padding(top = 16.dp),
                categories = state.genres.map { it.name },
                selectedChipIndex = state.selectedGenreIndex,
                onChipSelected = { index ->
                    viewModel.onGenreSelected(index)
                })
            AnimatedVisibility(route.type.ordinal != 1) {
                DiscoverMediaItems(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    route = route,
                    state = state,
                    listener = viewModel
                )
            }
            AnimatedVisibility(route.type.ordinal == 1) {
                TrendingContentList(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                  route = route,
                    state = state,
                )
            }
        }
    }
}
@Composable
private fun TrendingContentList(
    route: DiscoverRoute,
    state: DiscoverScreenState,
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
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(items) { item ->
                when (item) {
                    is DiscoverScreenState.MovieUiState -> {
                        TrendingMovieCard(
                            imgUrl = item.posterPath,
                            rating = String.format(Locale.getDefault(), "%.1f", item.rating),
                            movieTitle = item.title,
                            movieCategory =item.genres.get(0).name
                        )
                    }
                    is DiscoverScreenState.SeriesUiState -> {
                        TrendingMovieCard(
                            imgUrl = item.posterPath,
                            rating = String.format(Locale.getDefault(), "%.1f", item.rating),
                            movieTitle = item.title,
                            movieCategory = item.genres.get(0).name
                        )
                    }
                }
            }

        }
    }
}