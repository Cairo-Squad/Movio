package com.cairosquad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cairosquad.ui.home.content.HomeScreenContent
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val screenState by viewModel.screenState.collectAsState()

    HomeScreenContent(
        screenState = screenState,
        listener = viewModel,
    )
}

