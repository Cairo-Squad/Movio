package com.cairosquad.ui.home.content

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        MediaHorizontalPager(
            modifier = Modifier,
            mediaList = MediaHorizontalPagerItem.fromHomeSectionUiState(HomeScreenState.SectionUiState(
                movies = screenState.popularMovies,
                series = screenState.popularSeries
            )).take(7),
            initialPage = 3,
            onClickMedia = listener::onClickMedia
        )

        remember {
            listOf(
                MediaContentType.TOP_RATING,
                MediaContentType.TRENDING,
                MediaContentType.FREE_TO_WATCH,
                MediaContentType.UPCOMING,
                MediaContentType.MORE_RECOMMENDED
            )
        }.forEach { sectionType ->
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