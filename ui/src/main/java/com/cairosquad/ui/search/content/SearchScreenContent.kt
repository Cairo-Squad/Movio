package com.cairosquad.ui.search.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchScreenContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {
    when (state.screenStatus) {
        SearchScreenState.ScreenStatus.EXPLORE -> {
            ExploreScreenContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchScreenState.ScreenStatus.SEARCH -> {
            SearchContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchScreenState.ScreenStatus.RESULT -> {
            SearchResultContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchScreenState.ScreenStatus.LOADING -> {
            SearchLoadingContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }

        SearchScreenState.ScreenStatus.FAILED -> {
            SearchFailContent(
                modifier = modifier,
                state = state,
                listener = listener
            )
        }
    }
}