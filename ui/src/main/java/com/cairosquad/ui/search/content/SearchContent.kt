package com.cairosquad.ui.search.content

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.RecentSearchItem
import com.cairosquad.design_system.component.SectionHeader
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.searchviewmodel.SearchInteractionListener
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState
//import com.cairosquad.ui.R

@Composable
fun SearchContent(
    state: SearchUiState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    DisposableEffect(backPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener.onBackClicked()
            }
        }
        backPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {

        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(16.dp)
                .focusRequester(focusRequester),
            value = state.query,
            onValueChange = listener::onQueryTextChanged,
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
            trailingIcon = R.drawable.ic_close,
            onTrailingIconClick = { listener.onBackClicked() },
            keyboardActions = KeyboardActions(
                onDone = {
                    listener.onSearch(state.query)
                }
            )
        )

        SectionHeader(
            title = stringResource(R.string.recent_search),
            actionText = "Clear all",
            onActionClick = listener::onClearHistory
        )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(state.recentSearch) {
                RecentSearchItem(
                    recentItem = it,
                    query = state.query,
                    onRemoveHistoryItem = listener::onRemoveHistoryItem,
                    onRecentSearchItemClicked = listener::onRecentSearchItemClicked
                )
            }
        }
    }
}
