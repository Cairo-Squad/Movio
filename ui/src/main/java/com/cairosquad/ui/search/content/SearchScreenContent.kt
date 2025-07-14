package com.cairosquad.ui.search.content

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cairosquad.ui.movio_component.RefreshBox
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchScreenContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {

    Crossfade(state.screenStatus) {
        when (it) {
            SearchScreenState.ScreenStatus.EXPLORE -> {
                RefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = listener::onRefresh,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    ExploreScreenContent(
                        modifier = modifier,
                        state = state,
                        listener = listener
                    )
                }
            }

        SearchScreenState.ScreenStatus.SEARCH -> {
            SearchContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

            SearchScreenState.ScreenStatus.RESULT -> {
                RefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = listener::onRefresh,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    SearchResultContent(
                        modifier = modifier,
                        state = state,
                        listener = listener
                    )
                }
            }

            SearchScreenState.ScreenStatus.LOADING -> {
                RefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = listener::onRefresh,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    SearchLoadingContent(
                        modifier = modifier,
                        state = state,
                        listener = listener
                    )
                }
            }

            SearchScreenState.ScreenStatus.FAILED -> {
                RefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = listener::onRefresh,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    SearchFailContent(
                        modifier = modifier,
                        state = state,
                        listener = listener
                    )
                }
            }
        }
    }
}