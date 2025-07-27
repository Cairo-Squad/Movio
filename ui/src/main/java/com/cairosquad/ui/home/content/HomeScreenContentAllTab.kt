package com.cairosquad.ui.home.content

import android.content.res.Resources
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
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
                        listState = lazyListState,
                        onVisible = {
                            if (!screenState.sections.containsKey(sectionType)) {
                                listener.onSectionVisible(sectionType)
                            }
                        }
                    ) {
                        MediaSection(
                            modifier = Modifier.padding(bottom = 32.dp),
                            mediaList = MediaSectionItem.fromHomeSectionUiState(screenState.sections[sectionType]),
                            sectionTitle = stringResource(sectionType.titleId),
                            mediaSectionLayoutType = getMediaSectionLayout(sectionType),
                            onClickMedia = listener::onClickMedia,
                            seeAllAction = { listener.onClickSeeAll(sectionType, MediaType.BOTH) }
                        )
                    }
                }
            }
        }
}}

@Composable
fun SectionContainer(
    listState: LazyListState,
    onVisible: () -> Unit,
    content: @Composable () -> Unit
) {
    var triggered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                if (!triggered) {
                    val position = coordinates.positionInWindow()
                    val height = Resources.getSystem().displayMetrics.heightPixels.toFloat()
                    if (position.y in 0f..height) {
                        triggered = true
                        onVisible()
                    }
                }
            }
    ) {
        content()
    }
}