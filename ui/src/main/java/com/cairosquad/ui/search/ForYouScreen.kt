package com.cairosquad.ui.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.viewmodel.foryou.ForYouEffect
import com.cairosquad.viewmodel.foryou.ForYouInteractionListener
import com.cairosquad.viewmodel.foryou.ForYouState
import com.cairosquad.viewmodel.foryou.ForYouViewModel
import com.cairosquad.viewmodel.search.SearchEffect
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ForYouScreen(
    modifier: Modifier = Modifier,
    forYouViewModel: ForYouViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val forYou = forYouViewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        forYouViewModel.effect.collect { effect ->
            when (effect) {
                is ForYouEffect.NavigateToMovieDetails -> {
                    navController.navigate(MovieRoute(effect.movieId))
                }
                else -> Unit
            }
        }
    }
    RefreshBox(
        isRefreshing = forYou.value.isRefreshing,
        onRefresh = forYouViewModel::onRefresh,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Theme.color.surfaces.surface)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            AppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = stringResource(R.string.for_you),
                onBackButtonClicked = {
                    navController.popBackStack()
                },
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            MoviesList(
                modifier = Modifier.padding(16.dp),
                state = forYou.value,
                listener = forYouViewModel
            )

        }
    }
}

@Composable
private fun MoviesList(
    state: ForYouState,
    listener: ForYouInteractionListener,
    modifier: Modifier = Modifier
) {
    val movies = state.forYou.collectAsLazyPagingItems()
    AnimatedVisibility(
        visible = movies.itemCount == 0,
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
        visible = movies.itemCount > 0,
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
                        aspectRatio = 0.743f
                    )
                }
            }
        }
    }
}