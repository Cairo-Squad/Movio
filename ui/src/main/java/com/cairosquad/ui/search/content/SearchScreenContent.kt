package com.cairosquad.ui.search.content

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.component.RefreshBox
import com.cairosquad.viewmodel.searchviewmodel.SearchInteractionListener
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState

@Composable
fun SearchScreenContent(
    state: SearchUiState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {

    Crossfade(state.screenStatus) {
        when (it) {
            SearchUiState.ScreenStatus.EXPLORE -> {
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

            SearchUiState.ScreenStatus.SEARCH -> {
                SearchContent(
                    modifier = modifier,
                    state = state,
                    listener = listener
                )
            }

            SearchUiState.ScreenStatus.RESULT -> {
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

            SearchUiState.ScreenStatus.LOADING -> {
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

            SearchUiState.ScreenStatus.FAILED -> {
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