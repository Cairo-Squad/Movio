package com.cairosquad.ui.search

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.search.content.for_you.ForYouScreenContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.foryou.ForYouEffect
import com.cairosquad.viewmodel.foryou.ForYouScreenState.ScreenStatus
import com.cairosquad.viewmodel.foryou.ForYouViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ForYouScreen(
    modifier: Modifier = Modifier,
    forYouViewModel: ForYouViewModel = hiltViewModel(),
) {
    val state by forYouViewModel.screenState.collectAsState()
    val navController = LocalNavController.current
    val movies = state.forYou.collectAsLazyPagingItems()

    LaunchedEffect(movies.loadState) {
        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> {
                forYouViewModel.updateScreenStatus(ScreenStatus.LOADING)
            }

            is LoadState.NotLoading -> {
                forYouViewModel.updateScreenStatus(ScreenStatus.SUCCESS)
            }

            is LoadState.Error -> {
                forYouViewModel.updateScreenStatus(ScreenStatus.FAILED)
                val errorStatus = forYouViewModel.handleSearchException(state.error)
                forYouViewModel.updateErrorStatus(errorStatus)
            }
        }
    }

    ObserveAsEffect(forYouViewModel.effect) { effect ->
        when (effect) {
            is ForYouEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(effect.movieId))
            }
        }
    }
    ForYouScreenContent(
        listener = forYouViewModel,
        state = state,
        movies = movies,
        onBackAppBarClicked = {navController.popBackStack()},
        modifier = modifier,
    )
}

