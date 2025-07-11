package com.cairosquad.ui.search.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.StateMessage
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchUiState

@Composable
fun SearchFailContent(
    state: SearchUiState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(16.dp),
            value = state.query,
            onValueChange = listener::onQueryTextChanged,
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
            onFocusChanged = {
                if (it) {
                    listener.onClickSearchTextField()
                }
            },
            readOnly = true
        )

        StateMessage(
            imageDrawable = R.drawable.no_result,
            titleId = R.string.no_results_found,
            descriptionId = R.string.no_results_found_description
        )
    }
}