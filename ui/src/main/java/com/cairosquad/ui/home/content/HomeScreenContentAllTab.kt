package com.cairosquad.ui.home.content

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.movio_component.MediaHorizontalPager
import com.cairosquad.ui.movio_component.MediaHorizontalPagerItem
import com.cairosquad.ui.movio_component.MediaSection
import com.cairosquad.ui.movio_component.MediaSectionItem
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

@Composable
fun HomeScreenContentAllTab(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollState: ScrollState
) {
    val lazyListState = rememberLazyListState()

    val sections = remember {
        listOf(
            MediaContentType.TOP_RATING,
            MediaContentType.TRENDING,
            MediaContentType.FREE_TO_WATCH,
            MediaContentType.UPCOMING,
            MediaContentType.MORE_RECOMMENDED
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {
        MediaHorizontalPager(
            modifier = Modifier,
            mediaList = MediaHorizontalPagerItem.fromHomeSectionUiState(
                HomeScreenState.SectionUiState(
                    movies = screenState.popularMovies,
                    series = screenState.popularSeries
                )
            ).take(7),
            initialPage = 3,
            onClickMedia = listener::onClickMedia
        )
        LazyColumn(
            modifier = Modifier
                .heightIn(max = 10_000.dp),
            state = lazyListState
        ) {

            sections.forEach { sectionType ->
                item {
                    SectionContainer(
                        listState =lazyListState,
                        index = 0,
                        onVisible = {
                            if (!screenState.sections.containsKey(sectionType)) {
                                listener.onSectionVisible(sectionType)
                            }
                        }
                    ) {
                        val sectionState = screenState.sections[sectionType]

                        if (sectionState == null || sectionState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // CircularProgressIndicator()
                            }
                        } else {
                            MediaSection(
                                modifier = Modifier.padding(bottom = 32.dp),
                                mediaList = MediaSectionItem.fromHomeSectionUiState(sectionState),
                                sectionTitle = stringResource(sectionType.titleId),
                                mediaSectionLayoutType = getMediaSectionLayout(sectionType),
                                onClickMedia = listener::onClickMedia,
                                seeAllAction = {
                                    listener.onClickSeeAll(
                                        sectionType,
                                        MediaType.BOTH
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionContainer(
    listState: LazyListState,
    index: Int,
    onVisible: () -> Unit,
    content: @Composable () -> Unit
) {
    val layoutInfo = listState.layoutInfo
    val isVisible = remember(layoutInfo) {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.any { it.index == index }
        }
    }

    var triggered by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible.value) {
        if (isVisible.value && !triggered) {
            triggered = true
            onVisible()
        }
    }

    content()
}