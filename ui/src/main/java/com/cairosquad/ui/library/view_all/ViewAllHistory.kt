package com.cairosquad.ui.library.view_all

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.details.composable.BlurredCircle
import com.cairosquad.ui.library.component.UndoSnackBar
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.SwipeToDeleteContainer
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryEffect
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryInteractionListener
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryScreenState
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryViewModel
import java.util.Locale

@Composable
fun ViewAllHistory(
    viewModel: ViewAllHistoryViewModel = hiltViewModel()
) {

    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

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
    Box {
        ViewAllHistoryContent(
            screenState = uiState,
            listener = viewModel
        )
        UndoSnackBar(
            messageId = uiState.snackBarMessageId,
            isVisible = uiState.showSnackBar,
            onUndoClicked = viewModel::onUndoClicked,
        )
    }
}

@Composable
private fun ViewAllHistoryContent(
    screenState: ViewAllHistoryScreenState,
    listener: ViewAllHistoryInteractionListener
) {

    val movies = screenState.movies
    val series = screenState.series

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        BlurredCircle()
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
                    if (movies.isNotEmpty() || series.isNotEmpty()) {
                        HistoryItemsContent(movies, listener, series)
                    } else {
                        HistoryItemsEmpty()
                    }
                }

                ViewAllHistoryScreenState.SectionStatus.ERROR -> {

                }
            }
        }
    }
}

@Composable
private fun ColumnScope.HistoryItemsEmpty() {
    Spacer(Modifier.weight(1f))
    StateMessage(
        imageDrawable =
            if (Theme.isDark) com.cairosquad.design_system.R.drawable.favorite_list_empty_dark
            else com.cairosquad.design_system.R.drawable.favorite_list_empty,
        title = stringResource(R.string.no_watch_history_yet),
        description = stringResource(R.string.start_watching_movies_and_shows_and_we_ll_keep_track_of_your_viewing_history_here)
    )
    Spacer(Modifier.weight(1f))
}

@Composable
private fun HistoryItemsContent(
    movies: List<ViewAllHistoryScreenState.MovieUiState>,
    listener: ViewAllHistoryInteractionListener,
    series: List<ViewAllHistoryScreenState.SeriesUiState>
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
    ) {
        items(movies, key = {"${it.id} + movie"}) { movie ->
            SwipeToDeleteContainer(
                onDelete = { listener.onMovieDelete(movie.id) }
            ) {
                TrendingMovieCard(
                    modifier = Modifier.clickable {
                        listener.onMovieClicked(movie.id)
                    },
                    movieTitle = movie.title,
                    rating = String.format(
                        Locale.getDefault(),
                        "%.1f",
                        movie.rating
                    ),
                    imgUrl = movie.posterPath,
                    movieCategory = if (movie.genres.isNotEmpty()) movie.genres[0] else ""
                )
            }
        }
        items(series, key = {"${it.id} + series"}) { series ->
            SwipeToDeleteContainer(
                onDelete = { listener.onSeriesDelete(series.id) }
            ) {
                TrendingMovieCard(
                    modifier = Modifier.clickable {
                        listener.onSeriesClicked(series.id)
                    },
                    movieTitle = series.title,
                    rating = String.format(
                        Locale.getDefault(),
                        "%.1f",
                        series.rating
                    ),
                    imgUrl = series.posterPath,
                    movieCategory = if (series.genres.isNotEmpty()) series.genres[0] else ""
                )
            }
        }
    }
}