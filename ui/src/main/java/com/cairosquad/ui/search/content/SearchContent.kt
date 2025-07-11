package com.cairosquad.ui.search.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.searchviewmodel.SearchInteractionListener
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    listener: SearchInteractionListener
) {
    Box(modifier = modifier.fillMaxSize()) {
        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(16.dp),
            value = state.query,
            onValueChange = listener::onQueryTextChanged,
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
        )
    }
}
