package com.cairosquad.ui.library.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.details.composable.BlurredCircle
import com.cairosquad.ui.library.composable.UndoSnackBar
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.SwipeToDeleteContainer
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.list_content.ListContentEffect
import com.cairosquad.viewmodel.library.list_content.ListContentInteractionListener
import com.cairosquad.viewmodel.library.list_content.ListContentScreenState
import com.cairosquad.viewmodel.library.list_content.ListContentViewModel
import java.util.Locale

@Composable
fun ListScreen(
    listId: Long,
    listName: String,
) {
    val viewModel: ListContentViewModel =
        hiltViewModel<ListContentViewModel, ListContentViewModel.Factory> { factory ->
            factory.create(listId)
        }
    val navController = LocalNavController.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            ListContentEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ListContentEffect.OnMovieClicked -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is ListContentEffect.OnSeriesClicked -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }
        }
    }
    Box {
        ListScreenContent(
            uiState = uiState,
            listName = listName,
            listener = viewModel,
        )
        UndoSnackBar(
            messageId = uiState.snackMessageId,
            isVisible = uiState.showSnackBar,
            onUndoClicked = viewModel::onUndoClick
        )
    }
}

@Composable
private fun ListScreenContent(
    uiState: ListContentScreenState,
    listName: String,
    listener: ListContentInteractionListener
) {
    val movies = uiState.movies
    val series = uiState.series

    RefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = uiState.isRefreshing,
        onRefresh = listener::onRefresh,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surfaces.surface)
        ) {
            BlurredCircle()
            Column()
            {
                AppBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    title = listName,
                    onBackButtonClicked = listener::onBackClick,
                    onShareButtonClicked = null,
                    onFavoriteButtonClicked = null
                )
                when (uiState.screenStatus) {
                    ListContentScreenState.SectionStatus.LOADING -> {

                    }

                    ListContentScreenState.SectionStatus.SUCCESS -> {
                        if (movies.isNotEmpty() || series.isNotEmpty()) {
                            ListContent(
                                modifier = Modifier.padding(top = 12.dp),
                                movies = movies,
                                series = series,
                                listener = listener
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                            StateMessage(
                                imageDrawable =
                                    if (Theme.isDark) R.drawable.watch_later_empty_dark
                                    else R.drawable.watch_later_empty,
                                title = stringResource(R.string.watch_later_empty, listName),
                                description = stringResource(R.string.watch_later_empty_description)
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }

                    ListContentScreenState.SectionStatus.ERROR -> {

                    }
                }
            }
        }
    }
}

@Composable
private fun ListContent(
    movies: List<ListContentScreenState.MovieUiState>,
    series: List<ListContentScreenState.SeriesUiState>,
    listener: ListContentInteractionListener,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(movies, key = { it -> "${it.id} + ${it.title}" }
        ) { movie ->

            SwipeToDeleteContainer(
                onDelete = { listener.onMovieDelete(movie.id) },
            ) {
                TrendingMovieCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    imgUrl = movie.posterPath,
                    movieTitle = movie.title,
                    movieCategory = if (movie.genres.isNotEmpty()) movie.genres[0] else "",
                    rating = String.format(Locale.getDefault(), "%.1f", movie.rating)
                )
            }
        }
        items(
            series,
            key = { it -> "${it.id} + ${it.title}" }) { series ->
            SwipeToDeleteContainer(
                onDelete = { listener.onSeriesDelete(series.id) },
            ) {
                TrendingMovieCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    imgUrl = series.posterPath,
                    movieTitle = series.title,
                    movieCategory = if (series.genres.isNotEmpty()) series.genres[0] else "",
                    rating = String.format(Locale.getDefault(), "%.1f", series.rating)
                )
            }
        }
    }
}