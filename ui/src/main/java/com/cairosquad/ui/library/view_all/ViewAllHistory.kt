package com.cairosquad.ui.library.view_all

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryEffect
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryInteractionListener
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryScreenState
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryViewModel

@Composable
fun ViewAllHistory(
    viewModel: ViewAllHistoryViewModel = hiltViewModel()
) {

    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val movies = uiState.movies.collectAsLazyPagingItems()
    val series = uiState.series.collectAsLazyPagingItems()

    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ViewAllHistoryEffect.OnMovieClicked -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            ViewAllHistoryEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ViewAllHistoryEffect.OnSeriesClicked -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }
        }
    }

    ViewAllHistoryContent(
        screenState = uiState,
        movies = movies,
        series = series,
        listener = viewModel
    )
}

@Composable
private fun ViewAllHistoryContent(
    screenState: ViewAllHistoryScreenState,
    movies: LazyPagingItems<ViewAllHistoryScreenState.MovieUiState>,
    series: LazyPagingItems<ViewAllHistoryScreenState.SeriesUiState>,
    listener: ViewAllHistoryInteractionListener
) {
    Box(
        modifier = Modifier
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
                title = stringResource(R.string.history),
                onBackButtonClicked = listener::onBackClicked,
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            when (screenState.screenStatus) {
                ViewAllHistoryScreenState.SectionStatus.LOADING -> {

                }
                ViewAllHistoryScreenState.SectionStatus.SUCCESS -> {
                    if (movies.itemCount != 0 || series.itemCount != 0) {
                        LazyVerticalGrid (
                            modifier = Modifier.padding(top = 12.dp),
                            columns = GridCells.Adaptive(minSize = 101.33.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(movies.itemCount) { index ->
                                movies[index]?.let { movie ->
                                    MovieCard(
                                        modifier = Modifier.clickable {
                                            listener.onMovieClicked(movie.id)
                                        },
                                        title = movie.title,
                                        vote = movie.rating,
                                        imgUrl = movie.posterPath,
                                        width = null,
                                        aspectRatio = 0.775f,
                                    )
                                }
                            }
                            items(series.itemCount) { index ->
                                series[index]?.let { series ->
                                    MovieCard(
                                        modifier = Modifier.clickable {
                                            listener.onSeriesClicked(series.id)
                                        },
                                        title = series.title,
                                        vote = series.rating,
                                        imgUrl = series.posterPath,
                                        width = null,
                                        aspectRatio = 0.775f
                                    )
                                }
                            }
                        }
                    }
                    else {
                        Spacer(Modifier.weight(1f))
                        StateMessage(
                            imageDrawable = com.cairosquad.design_system.R.drawable.favorite_list_empty,
                            title = stringResource(R.string.no_watch_history_yet),
                            description = stringResource(R.string.start_watching_movies_and_shows_and_we_ll_keep_track_of_your_viewing_history_here)
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
                ViewAllHistoryScreenState.SectionStatus.ERROR -> {

                }
            }
        }
    }
}