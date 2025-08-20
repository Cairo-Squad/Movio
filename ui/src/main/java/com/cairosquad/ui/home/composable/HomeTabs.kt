package com.cairosquad.ui.home.composable

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cairosquad.ui.home.content.HomeScreenContentCategoriesTab
import com.cairosquad.ui.home.content.HomeScreenContentMoviesTab
import com.cairosquad.ui.home.content.HomeScreenContentSeriesTab
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.CATEGORIES
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.MOVIES
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.TV_SHOWS

@Composable
fun HomeTabs(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    lazyListState: LazyListState,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        modifier = modifier,
        targetState = screenState.selectedTab
    ) { selectedTab ->
        when (selectedTab) {
            MOVIES -> HomeScreenContentMoviesTab(screenState, listener, lazyListState)
            TV_SHOWS -> HomeScreenContentSeriesTab(screenState, listener, lazyListState)
            CATEGORIES -> HomeScreenContentCategoriesTab(screenState, listener, lazyGridState)
        }
    }
}