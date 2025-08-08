package com.cairosquad.ui.library.list

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
import androidx.compose.foundation.layout.Spacer
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
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
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
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(16.dp),
            visible = uiState.showSnackBar,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 600)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 600)
            )
        ) {
            SnackBar(
                imageVector = ImageVector.vectorResource(com.cairosquad.design_system.R.drawable.danger),
                message = stringResource(uiState.snackMessageId),
                action = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = viewModel::onUndoClicked)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(com.cairosquad.ui.R.string.undo),
                            style = Theme.textStyle.label.smallRegular14,
                            color = Theme.color.brand.primary
                        )
                    }
                }
            )
        }
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

            Column()
            {
                AppBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    title = listName,
                    onBackButtonClicked = listener::onBackClicked,
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
                                imageDrawable = R.drawable.watch_later_empty,
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
                    movieCategory = movie.trailerPath,
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
                    movieCategory = series.trailerPath,
                    rating = String.format(Locale.getDefault(), "%.1f", series.rating)
                )
            }
        }
    }
}