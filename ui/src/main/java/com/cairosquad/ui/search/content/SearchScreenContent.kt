package com.cairosquad.ui.search.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchUiState

@Composable
fun SearchScreenContent(
    state: SearchUiState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {
    when (state.screenStatus) {
        SearchUiState.ScreenStatus.EXPLORE -> {
            ExploreScreenContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchUiState.ScreenStatus.SEARCH -> {
            SearchContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchUiState.ScreenStatus.RESULT -> {
            SearchResultContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchUiState.ScreenStatus.LOADING -> {
            SearchLoadingContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchUiState.ScreenStatus.FAILED -> {
            SearchFailContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }
    }
}