package com.cairosquad.ui.search.content.for_you

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.search.composable.MoviesList
import com.cairosquad.viewmodel.foryou.ForYouInteractionListener
import com.cairosquad.viewmodel.foryou.ForYouScreenState

@Composable
fun ForYouScreenContent(
    listener: ForYouInteractionListener,
    state: ForYouScreenState,
    movies: LazyPagingItems<ForYouScreenState.MovieUiState>,
    onBackAppBarClicked: () -> Unit,
    modifier: Modifier = Modifier
    ){
    RefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = listener::onRefresh,
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
                onBackButtonClicked = onBackAppBarClicked,
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            MoviesList(
                modifier = Modifier.padding(8.dp),
                state = state,
                listener = listener,
                movies = movies
            )

        }
    }
}