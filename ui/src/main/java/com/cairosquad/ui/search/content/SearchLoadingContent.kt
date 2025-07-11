package com.cairosquad.ui.search.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.LoadingMovieCard
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.searchviewmodel.SearchInteractionListener
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState

@Composable
fun SearchLoadingContent(
    state: SearchUiState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        modifier = modifier.background(Theme.color.surfaces.surface),
        columns = GridCells.Adaptive(minSize = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        stickyHeader {
            InputField(
                modifier = Modifier
                    .background(Theme.color.surfaces.surface),
                value = state.query,
                onValueChange = {  },
                placeholder = stringResource(R.string.search),
                leadingIcon = R.drawable.search_bottom_nav,
                onFocusChanged = { if (it) { listener.onClickSearchTextField() } },
                readOnly = true
            )
        }
        items(20) {
            LoadingMovieCard()
        }
    }
}