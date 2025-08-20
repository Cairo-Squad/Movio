package com.cairosquad.ui.see_all_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.see_all_screen.content.SeeAllScreenContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.see_all.SeeAllEffect
import com.cairosquad.viewmodel.see_all.SeeAllScreenState
import com.cairosquad.viewmodel.see_all.SeeAllViewModel
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

@Composable
fun SeeAllScreen(
    contentType: MediaContentType,
    mediaType: MediaType,
    viewModel: SeeAllViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    val navController = LocalNavController.current
    val media = state.mediaList.collectAsLazyPagingItems()
    LaunchedEffect(contentType, mediaType) {
        viewModel.loadData(
            contentType = contentType, mediaType = mediaType
        )
    }
    LaunchedEffect(media.loadState) {
        when (val state = media.loadState.refresh) {
            is androidx.paging.LoadState.Loading -> {
                viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.LOADING)
            }

            is androidx.paging.LoadState.NotLoading -> {
                if (media.itemCount == 0) {
                    viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.Empty)
                } else {
                    viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.SUCCESS)
                }
            }

            is androidx.paging.LoadState.Error -> {
                viewModel.updateScreenStatus(SeeAllScreenState.ScreenStatus.FAILED)
                val errorStatus = viewModel.handleHomeException(state.error)
                viewModel.updateErrorStatus(errorStatus)
            }
        }
    }
    ObserveAsEffect(viewModel.effect) { effect ->
        effectDiscoverHandler(
            effect, navController
        )
    }
    SeeAllScreenContent(
        contentType = contentType,
        media = media,
        state = state,
        listener = viewModel,
        onBackAppBarClicked = { navController.popBackStack() }
    )

}

private fun effectDiscoverHandler(
    effect: SeeAllEffect, navController: NavController
) {

    when (effect) {
        SeeAllEffect.NavigateBack -> {
            navController.popBackStack()
        }

        is SeeAllEffect.NavigateMediaDetails -> {
            if (effect.isMovie) {
                navController.navigate(MovieRoute(effect.mediaId))
            } else {
                navController.navigate(SeriesRoute(effect.mediaId))
            }
        }
    }
}