package com.cairosquad.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cairosquad.ui.search.content.SearchScreenContent
import com.cairosquad.viewmodel.searchviewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {

    val state by viewModel.uiState.collectAsState()

    SearchScreenContent(
        state = state,
        listener = viewModel,
        modifier = modifier
    )
}

