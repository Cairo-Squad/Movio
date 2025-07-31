package com.cairosquad.ui.home.content

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun HomeScreenContentSeriesTab(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollState: ScrollState
) {
    val lazyListState = rememberLazyListState()
    val sections = remember {
        listOf(
            MediaContentType.TOP_RATING,
            MediaContentType.AIRING_TODAY,
            MediaContentType.ON_TV,
            MediaContentType.MORE_RECOMMENDED
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        MediaHorizontalPager(
            modifier = Modifier,
            mediaList = screenState.popularSeries
                .map(MediaHorizontalPagerItem::fromHomeMediaUiState)
                .take(7),
            initialPage = 3,
            onClickMedia = listener::onClickMedia
        )
        LazyColumn(
            modifier = Modifier.heightIn(max = 10_000.dp), state = lazyListState
        ) {
            sections.forEach { sectionType ->
                item {
                    SectionContainer(
                        listState = lazyListState, index = 0, onVisible = {
                            if (!screenState.sections.containsKey(sectionType)) {
                                listener.onSectionVisible(sectionType)
                            }
                        }) {
                        val sectionState = screenState.sections[sectionType]

                        if (sectionState == null || sectionState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {

                            }
                        } else {
                            MediaSection(
                                modifier = Modifier.padding(bottom = 32.dp),
                                mediaList = sectionState.series.map(MediaSectionItem::fromHomeMediaUiState),
                                sectionTitle = stringResource(sectionType.titleId),
                                mediaSectionLayoutType = getMediaSectionLayout(sectionType),
                                onClickMedia = listener::onClickMedia,
                                seeAllAction = {
                                    listener.onClickSeeAll(
                                        sectionType, MediaType.SERIES
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}