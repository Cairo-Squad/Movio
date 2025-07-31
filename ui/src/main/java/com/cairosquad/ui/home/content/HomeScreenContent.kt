package com.cairosquad.ui.home.content

import HomeScreenContentCategoriesTab
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.AppBar
import com.cairosquad.ui.movio_component.MediaSectionLayoutType
import com.cairosquad.ui.see_all_screen.SeeAllLoadingScreen
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.util.MediaContentType

@Composable
fun HomeScreenContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
) {
    val scrollState = rememberScrollState()
    RefreshBox(
        isRefreshing = screenState.isRefreshing,
        onRefresh = { listener.onRefresh() }
    ) {

        Crossfade(targetState = screenState.screenStatus) { screenStatus ->
            when (screenStatus) {
                HomeScreenState.ScreenStatus.FAILED -> {
                    HomeFailContent(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(top = 48.dp)
                            .padding(top = 36.dp),
                        errorStatus = screenState.errorStatus,
                    )
                }

                HomeScreenState.ScreenStatus.LOADING -> {
                    SeeAllLoadingScreen(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(top = 48.dp)
                            .padding(top = 36.dp)
                    )
                }

                else -> {
                    Crossfade(screenState.selectedTab) { selectedTab ->
                        when (selectedTab) {

                            HomeScreenState.Tab.MOVIES -> {
                                HomeScreenContentMoviesTab(
                                    screenState = screenState,
                                    listener = listener,
                                    scrollState = scrollState
                                )
                            }

                            HomeScreenState.Tab.TV_SHOWS -> {
                                HomeScreenContentSeriesTab(
                                    screenState = screenState,
                                    listener = listener,
                                    scrollState = scrollState
                                )
                            }

                            HomeScreenState.Tab.CATEGORIES -> {
                                HomeScreenContentCategoriesTab(
                                    modifier = Modifier
                                        .statusBarsPadding()
                                        .padding(top = 48.dp)
                                        .padding(top = 36.dp),
                                    screenState = screenState,
                                    listener = listener,
                                    scrollState = scrollState
                                )
                            }
                        }
                    }
                }
            }
        }

        TobContent(
            screenState = screenState,
            listener = listener,
            scrollState = scrollState
        )
    }
}

@Composable
private fun TobContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 275.dp.toPx() }
    val progress = (scrollState.value / scrollThresholdPx).coerceIn(0f, 1f)
    val animatedEndColor = lerp(
        start = Color.Transparent,
        stop = Theme.color.surfaces.surface,
        fraction = progress
    )
    val animatedBrush = Brush.verticalGradient(
        colors = listOf(Theme.color.surfaces.surface, animatedEndColor)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(animatedBrush)
    ) {
        AppBar(modifier = Modifier.statusBarsPadding())

        val tabsList = remember {
            listOf(
                R.string.movies,
                R.string.tv_shows,
                R.string.categories,
            )
        }

        TabRow(
            modifier = Modifier,
            tabs = tabsList.map { stringResource(it) },
            selectedTabIndex = screenState.selectedTab.ordinal,
            onTabSelected = listener::onClickTab,
            scrollState = scrollState
        )
    }
}

fun getMediaSectionLayout(mediaContentType: MediaContentType): MediaSectionLayoutType {
    return when (mediaContentType) {
        MediaContentType.TOP_RATING -> MediaSectionLayoutType.LazyRow
        MediaContentType.TRENDING -> MediaSectionLayoutType.LazyHorizontalGrid(3)
        MediaContentType.FREE_TO_WATCH -> MediaSectionLayoutType.LazyRow
        MediaContentType.UPCOMING -> MediaSectionLayoutType.LazyRow
        MediaContentType.NOW_PLAYING -> MediaSectionLayoutType.LazyRow
        MediaContentType.MORE_RECOMMENDED -> MediaSectionLayoutType.LazyVerticalGrid(158)
        MediaContentType.AIRING_TODAY -> MediaSectionLayoutType.LazyRow
        MediaContentType.ON_TV -> MediaSectionLayoutType.LazyRow
    }
}