package com.cairosquad.ui.search.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.SectionHeader
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.searchviewmodel.SearchInteractionListener
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState

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
            onTrailingIconClick = {

            },
            keyboardActions = KeyboardActions(
                onDone = {
                    listener.onSearch(state.query)
                }
            )
        )

        SectionHeader(
            title = "Recent Search",
            actionText = "Clear all",
            onActionClick = listener::onClearHistory
        )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(state.recentSearch) {
                RecentSearchItem(
                    recentItem = it,
                    onRemoveHistoryItem = listener::onRemoveHistoryItem,
                    onRecentSearchItemClicked = listener::onRecentSearchItemClicked
                )
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    modifier: Modifier = Modifier,
    recentItem: String,
    onRecentSearchItemClicked: (String) -> Unit,
    onRemoveHistoryItem: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = { onRecentSearchItemClicked(recentItem) }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                imageVector = ImageVector.vectorResource(com.cairosquad.ui.R.drawable.recent),
                contentDescription = stringResource(com.cairosquad.ui.R.string.recent_item_icon),
                colorFilter = ColorFilter.tint(Theme.color.surfaces.onSurfaceVariant)
            )
            BasicText(
                modifier = Modifier.weight(1f),
                text = recentItem,
                style = Theme.textStyle.label.smallRegular14.copy(color = Theme.color.surfaces.onSurfaceVariant)
            )
        }
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { onRemoveHistoryItem(recentItem) })
                .size(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_close),
            contentDescription = stringResource(com.cairosquad.ui.R.string.clear_search_result),
            colorFilter = ColorFilter.tint(Theme.color.surfaces.onSurfaceVariant)
        )
    }
}