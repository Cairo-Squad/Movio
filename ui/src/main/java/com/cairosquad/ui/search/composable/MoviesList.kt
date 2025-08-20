package com.cairosquad.ui.search.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.search.content.for_you.EmptyMoviesContent
import com.cairosquad.ui.search.content.for_you.ForYouFailedContent
import com.cairosquad.ui.search.content.for_you.ForYouLoadingContent
import com.cairosquad.ui.search.content.for_you.MoviesGridContent
import com.cairosquad.viewmodel.foryou.ForYouInteractionListener
import com.cairosquad.viewmodel.foryou.ForYouScreenState
import com.cairosquad.viewmodel.foryou.ForYouScreenState.MovieUiState
import com.cairosquad.viewmodel.foryou.ForYouScreenState.ScreenStatus

@Composable
fun MoviesList(
    state: ForYouScreenState,
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
                    listener = listener,
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