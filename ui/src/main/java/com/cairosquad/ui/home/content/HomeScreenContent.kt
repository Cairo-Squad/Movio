package com.cairosquad.ui.home.content

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.ui.home.composable.HomeLoading
import com.cairosquad.ui.home.composable.HomeTabs
import com.cairosquad.ui.movio_component.MediaSectionLayoutType
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeScreenState.DataRequestStatus.FAILED
import com.cairosquad.viewmodel.home.HomeScreenState.DataRequestStatus.LOADING
import com.cairosquad.viewmodel.home.HomeScreenState.DataRequestStatus.SUCCESS
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.CATEGORIES
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

        Crossfade(targetState = if (screenState.isRefreshing) LOADING else screenState.dataRequestStatus) { dataRequestStatus ->
            when (dataRequestStatus) {
                LOADING -> HomeLoading()
                FAILED -> HomeFailContent(screenState.errorStatus, listener)
                SUCCESS -> HomeTabs(screenState, listener, lazyListState, lazyGridState)
            }
        }

        TopContent(
            screenState,
            listener,
            scrollProgress
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