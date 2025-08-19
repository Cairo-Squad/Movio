package com.cairosquad.ui.search.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.RecentSearchItem
import com.cairosquad.ui.movio_component.RecommendationSearchItem
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    BackHandler(enabled = true) {
        listener.onBackClick()
    }

    LaunchedEffect(state.screenStatus) {
        if (state.screenStatus != SearchScreenState.ScreenStatus.SEARCH) return@LaunchedEffect
        focusRequester.requestFocus()
    }

    Column(modifier = modifier.fillMaxSize()) {

        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(16.dp)
                .focusRequester(focusRequester),
            value = state.query,
            onValueChange = listener::onQueryTextChanged,
            placeholder = stringResource(R.string.search_with_dotes_ahead),
            leadingIcon = R.drawable.search_bottom_nav,
            trailingIcon =   if(state.query.isNotEmpty()) R.drawable.ic_close else null,
            onTrailingIconClick = { listener.onCancelSearch() },
            keyboardActions = KeyboardActions(onDone = { listener.onSearch() })
        )
        if (state.recentSearch.isNotEmpty() && state.query.isBlank()) {
            SectionHeader(
                title = stringResource(R.string.recent_search),
                actionText = stringResource(R.string.clear_all),
                onActionClick = listener::onClearHistory
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (state.query.isBlank()) {
                items(state.recentSearch) {
                    RecentSearchItem(
                        recentItem = it,
                        query = state.query,
                        onRemoveHistoryItem = listener::onRemoveHistoryItem,
                        onRecentSearchItemClicked = listener::onRecentSearchItemClick
                    )
                }
            } else {
                items(state.searchRecommendation) { item ->
                    val isRecent = item in state.recentSearch
                    RecommendationSearchItem(
                        recommendationItem = item,
                        query = state.query,
                        isRecentSearch = isRecent,
                        onSearchRecommendationItemClicked = listener::onRecentSearchItemClick
                    )
                }
            }
        }
    }
}