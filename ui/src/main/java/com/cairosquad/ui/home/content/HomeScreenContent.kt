package com.cairosquad.ui.home.content

import HomeScreenContentCategoriesTab
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.cairosquad.ui.utils.LoadingMoviesGrid
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeScreenState.DateRequestStatus.FAILED
import com.cairosquad.viewmodel.home.HomeScreenState.DateRequestStatus.LOADING
import com.cairosquad.viewmodel.home.HomeScreenState.DateRequestStatus.SUCCESS
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.CATEGORIES
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.MOVIES
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.TV_SHOWS
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaContentType.AIRING_TODAY
import com.cairosquad.viewmodel.util.MediaContentType.FREE_TO_WATCH
import com.cairosquad.viewmodel.util.MediaContentType.MORE_RECOMMENDED
import com.cairosquad.viewmodel.util.MediaContentType.NOW_PLAYING
import com.cairosquad.viewmodel.util.MediaContentType.ON_TV
import com.cairosquad.viewmodel.util.MediaContentType.TOP_RATING
import com.cairosquad.viewmodel.util.MediaContentType.TRENDING
import com.cairosquad.viewmodel.util.MediaContentType.UPCOMING

@Composable
fun HomeScreenContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
) {
    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()

    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 32.dp.toPx() }

    val lazyListScrollProgress by remember {
        getLazyListScrollProgressToThreshold(
            lazyListState,
            with(density) { 300.dp.toPx() })
    }
    val lazyGridScrollProgress by remember {
        getLazyGridScrollProgressToThreshold(
            lazyGridState,
            scrollThresholdPx
        )
    }
    val scrollProgress =
        if (screenState.selectedTab == CATEGORIES) lazyGridScrollProgress else lazyListScrollProgress

    RefreshBox(
        isRefreshing = screenState.isRefreshing,
        onRefresh = { listener.onRefresh() }
    ) {

        Crossfade(targetState = screenState.dataRequestStatus) { dataRequestStatus ->
            when (dataRequestStatus) {
                LOADING -> HomeLoading()
                FAILED -> HomeFailContent(screenState.errorStatus, listener)
                SUCCESS -> HomeTabs(screenState, listener, lazyListState, lazyGridState)
            }
        }

        TobContent(
            screenState,
            listener,
            scrollProgress
        )
    }
}

@Composable
private fun HomeTabs(
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

@Composable
private fun HomeLoading(
    modifier: Modifier = Modifier,
) {
    LoadingMoviesGrid(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 48.dp)
            .padding(top = 36.dp)
    )
}

@Composable
private fun TobContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollProgress: Float,
    modifier: Modifier = Modifier
) {
    val topColor = lerp(
        if (Theme.isDark) Color.Black else Color.White,
        Theme.color.surfaces.surface,
        scrollProgress
    )

    val bottomColor = lerp(
        if (screenState.selectedTab == CATEGORIES)
            Theme.color.surfaces.surface
        else
            Color.Transparent,
        Theme.color.surfaces.surface,
        scrollProgress
    )

    val animatedBrush = Brush.verticalGradient(
        colors = listOf(topColor, bottomColor)
    )

    val tabsNamesResId = remember {
        listOf(
            R.string.movies,
            R.string.tv_shows,
            R.string.categories,
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(animatedBrush)
    ) {
        AppBar(
            modifier = Modifier.statusBarsPadding(),
            userImage = screenState.profileImage
        )

        TabRow(
            modifier = Modifier,
            tabs = tabsNamesResId.map { stringResource(it) },
            selectedTabIndex = screenState.selectedTab.ordinal,
            onTabSelected = listener::onClickTab,
            scrollProgress = scrollProgress,
            tabColorWithScroll = Theme.color.brand.onPrimaryContainer,
            tabColorWithNoScroll = Theme.color.brand.onPrimary,
            indicatorColorWithScroll = Theme.color.gradiant.horizontalCategoriesGradient,
            indicatorColorWithNoScroll = Theme.color.gradiant.horizontalGradient
        )
    }
}

private fun getLazyListScrollProgressToThreshold(
    lazyListState: LazyListState,
    scrollThresholdPx: Float
): State<Float> {
    return derivedStateOf {
        if (lazyListState.layoutInfo.totalItemsCount == 0) return@derivedStateOf 0f
        if (lazyListState.firstVisibleItemIndex > 0) return@derivedStateOf 1f
        minOf(lazyListState.firstVisibleItemScrollOffset / scrollThresholdPx, 1f)
    }
}

private fun getLazyGridScrollProgressToThreshold(
    lazyGridState: LazyGridState,
    scrollThresholdPx: Float
): State<Float> {
    return derivedStateOf {
        if (lazyGridState.layoutInfo.totalItemsCount == 0) return@derivedStateOf 0f
        if (lazyGridState.firstVisibleItemIndex > 0) return@derivedStateOf 1f
        minOf(lazyGridState.firstVisibleItemScrollOffset / scrollThresholdPx, 1f)
    }
}

fun getMediaSectionLayout(mediaContentType: MediaContentType): MediaSectionLayoutType {
    return when (mediaContentType) {
        TOP_RATING -> MediaSectionLayoutType.LazyRow
        TRENDING -> MediaSectionLayoutType.LazyHorizontalGrid(3)
        FREE_TO_WATCH -> MediaSectionLayoutType.LazyRow
        UPCOMING -> MediaSectionLayoutType.LazyRow
        NOW_PLAYING -> MediaSectionLayoutType.LazyRow
        MORE_RECOMMENDED -> MediaSectionLayoutType.LazyVerticalGrid(158)
        AIRING_TODAY -> MediaSectionLayoutType.LazyRow
        ON_TV -> MediaSectionLayoutType.LazyRow
    }
}