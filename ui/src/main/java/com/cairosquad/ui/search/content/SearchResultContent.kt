package com.cairosquad.ui.search.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchResultContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val movies = state.movies.collectAsLazyPagingItems()
    val artists = state.artists.collectAsLazyPagingItems()
    val series = state.series.collectAsLazyPagingItems()
    BackHandler(enabled = true) {
        listener.onBackClick()
    }

    val selectedTabIndex = state.selectedTabIndex
    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 275.dp.toPx() }
    val progress = (scrollState.value / scrollThresholdPx).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(bottom = 12.dp)
                .padding(horizontal = 16.dp),
            isFocusEnabled = false,
            onClick = listener::onClickSearchTextField,
            value = state.query,
            onValueChange = { },
            placeholder = stringResource(R.string.search_with_dotes_ahead),
            leadingIcon = R.drawable.search_bottom_nav,
            trailingIcon = if (state.query.isNotEmpty()) R.drawable.ic_close else null,
            onTrailingIconClick = { listener.onCancelSearch() },
            readOnly = true
        )
        TabRow(
            modifier = Modifier,
            tabs = listOf(
                stringResource(R.string.top_Results),
                stringResource(R.string.movies),
                stringResource(R.string.series),
                stringResource(R.string.artists),
            ),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { listener.onTabSelected(it) },
            scrollProgress = progress,
            tabColorWithScroll = Theme.color.brand.onPrimaryContainer,
            tabColorWithNoScroll = Theme.color.brand.onPrimaryContainer,
            indicatorColorWithNoScroll = Theme.color.gradiant.horizontalCategoriesGradient,
            indicatorColorWithScroll = Theme.color.gradiant.horizontalCategoriesGradient

        )

        when (selectedTabIndex) {
            0 -> {
                AllResultsTabContent(movies = movies, listener = listener, state = state)
            }

            1 -> {
                MoviesTabContent(movies = movies, listener = listener, state = state)
            }

            2 -> {
                SeriesTabContent(series = series, listener = listener, state = state)
            }

            3 -> {
                ArtistsTabContent(artist = artists, listener = listener, state = state)
            }
        }
    }
}