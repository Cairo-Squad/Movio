package com.cairosquad.ui.search

import android.annotation.SuppressLint
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.viewmodel.exception.ErrorStatus
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
                modifier = Modifier.padding(16.dp),
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
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.screenStatus == ScreenStatus.LOADING -> {
                ForYouLoadingContent(
                    state = state,
                    listener = listener,
                    modifier = modifier
                )
            }
            state.isEmpty -> {
                EmptyMoviesContent(modifier)
            }
            state.screenStatus == ScreenStatus.FAILED -> {
                ForYouFailedContent(
                    state = state,
                    listener = listener,
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

@Composable
private fun ForYouLoadingContent(
    state: ForYouState,
    listener: ForYouInteractionListener,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.background(Theme.color.surfaces.surface),
        columns = GridCells.Adaptive(minSize = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(20) {
            LoadingMovieCard()
        }
    }
}

@Composable
private fun EmptyMoviesContent(modifier: Modifier = Modifier) {
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

@Composable
private fun MoviesGridContent(
    movies: LazyPagingItems<MovieUiState>,
    listener: ForYouInteractionListener,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
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

@Composable
private fun ForYouFailedContent(
    state: ForYouState,
    listener: ForYouInteractionListener,
    errorStatus: ErrorStatus?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (errorStatus) {
            ErrorStatus.NO_INTERNET -> StateMessage(
                imageDrawable = R.drawable.no_internet,
                titleId = R.string.no_internet_connection,
                descriptionId = R.string.no_internet_connection
            )

//            ErrorStatus.NETWORK_ERROR -> StateMessage(
//                imageDrawable = R.drawable.error_state,
//                titleId = R.string.network_error,
//                descriptionId = R.string.network_error_description
//            )
//
//            else -> StateMessage(
//                imageDrawable = R.drawable.error_state,
//                titleId = R.string.error_occurred,
//                descriptionId = R.string.please_try_again_later
//            )
            ErrorStatus.NETWORK_ERROR -> TODO()
            ErrorStatus.UNKNOWN_ERROR -> TODO()
            ErrorStatus.UNAUTHORIZED -> TODO()
            ErrorStatus.EMPTY -> TODO()
            ErrorStatus.PARSING_ERROR -> TODO()
            null -> TODO()
        }
    }
}