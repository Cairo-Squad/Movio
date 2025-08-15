package com.cairosquad.ui.library.view_all

import androidx.compose.animation.core.tween
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
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.view_all_favorite.ViewAllFavoriteEffect
import com.cairosquad.viewmodel.library.view_all_favorite.ViewAllFavoriteScreenState
import com.cairosquad.viewmodel.library.view_all_favorite.ViewAllFavoriteViewModel
import java.util.Locale

@Composable
fun ViewAllFavorite(
    viewModel: ViewAllFavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            ViewAllFavoriteEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ViewAllFavoriteEffect.OnMovieClicked -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is ViewAllFavoriteEffect.OnSeriesClicked -> {
                navController.navigate(MovieRoute(effect.seriesId))

            }
        }
    }
    Box {
        ViewAllFavoriteContent(
            uiState = uiState,
            listener = viewModel,
        )
        UndoSnackBar(
            messageId = uiState.snackMessageId,
            isVisible = uiState.showSnackBar,
            onUndoClicked = viewModel::onUndoClicked
        )
    }
}


@Composable
fun ViewAllFavoriteContent(
    uiState: ViewAllFavoriteScreenState,
    listener: ViewAllFavoriteViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        BlurredCircle()
        Column {
            AppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = stringResource(R.string.favorite),
                onBackButtonClicked = listener::onBackClicked,
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            if (uiState.movies.isNotEmpty() || uiState.series.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
                ) {

                    items(
                        uiState.movies,
                        key = { "${it.id} + movie" }
                    ) { movie ->
                        SwipeToDeleteContainer(
                            modifier = Modifier.animateItem(tween(200)),
                            onDelete = { listener.onMovieDelete(movie.id) },
                        ) {
                            TrendingMovieCard(
                                modifier = Modifier
                                    .clickable(onClick = { listener.onMovieClicked(movie.id) }),
                                imgUrl = movie.posterPath,
                                movieTitle = movie.title,
                                movieCategory = movie.genres[0],
                                rating = String.format(Locale.getDefault(), "%.1f", movie.rating)
                            )
                        }
                    }
                    items(
                        uiState.series,
                        key = { "${it.id} + series" }
                    ) { series ->
                        SwipeToDeleteContainer(
                            modifier = Modifier.animateItem(tween(200)),
                            onDelete = { listener.onSeriesDelete(series.id) },
                        ) {
                            TrendingMovieCard(
                                modifier = Modifier
                                    .clickable(onClick = { listener.onSeriesDelete(series.id) }),
                                imgUrl = series.posterPath,
                                movieTitle = series.title,
                                movieCategory = series.genres[0],
                                rating = String.format(Locale.getDefault(), "%.1f", series.rating)
                            )
                        }
                    }
                }

            } else {
                Spacer(Modifier.weight(1f))
                StateMessage(
                    imageDrawable =
                        if (Theme.isDark) com.cairosquad.design_system.R.drawable.favorite_list_empty_dark
                        else com.cairosquad.design_system.R.drawable.favorite_list_empty,
                    title = stringResource(com.cairosquad.design_system.R.string.favorites_list_empty),
                    description = stringResource(com.cairosquad.design_system.R.string.favorite_list_empty_description)
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}