package com.cairosquad.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.search.content.for_you.EmptyMoviesContent
import com.cairosquad.ui.search.content.for_you.ForYouFailedContent
import com.cairosquad.ui.search.content.for_you.ForYouLoadingContent
import com.cairosquad.ui.search.content.for_you.MoviesGridContent
import com.cairosquad.viewmodel.foryou.ForYouEffect
import com.cairosquad.viewmodel.foryou.ForYouInteractionListener
import com.cairosquad.viewmodel.foryou.ForYouState
import com.cairosquad.viewmodel.foryou.ForYouState.MovieUiState
import com.cairosquad.viewmodel.foryou.ForYouState.ScreenStatus
import com.cairosquad.viewmodel.foryou.ForYouViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ForYouScreen(
    modifier: Modifier = Modifier,
    forYouViewModel: ForYouViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val forYou = forYouViewModel.screenState.collectAsState()
    val movies = forYou.value.forYou.collectAsLazyPagingItems()

    LaunchedEffect(movies.loadState) {
        when (val state = movies.loadState.refresh) {
            is androidx.paging.LoadState.Loading -> {
                forYouViewModel.updateScreenStatus(ScreenStatus.LOADING)
            }

            is androidx.paging.LoadState.NotLoading -> {
                forYouViewModel.updateScreenStatus(ScreenStatus.SUCCESS)
            }

            is androidx.paging.LoadState.Error -> {
                forYouViewModel.updateScreenStatus(ScreenStatus.FAILED)
                val errorStatus = forYouViewModel.handleSearchException(state.error)
                forYouViewModel.updateErrorStatus(errorStatus)
            }
        }
    }

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
                modifier = Modifier.padding(8.dp),
                state = forYou.value,
                listener = forYouViewModel,
                movies = movies
            )

        }
    }
}

@Composable
private fun MoviesList(
    state: ForYouState,
    listener: ForYouInteractionListener,
    movies: LazyPagingItems<MovieUiState>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        when {
            state.screenStatus == ScreenStatus.LOADING -> {
                ForYouLoadingContent(
                    modifier = modifier
                )
            }

            state.isEmpty -> {
                EmptyMoviesContent(modifier)
            }

            state.screenStatus == ScreenStatus.FAILED -> {
                ForYouFailedContent(
                    errorStatus = state.errorStatus,
                    modifier = modifier
                )
            }

            else -> {
                MoviesGridContent(
                    movies = movies,
                    listener = listener,
                    modifier = modifier
                )
            }
        }
    }
}