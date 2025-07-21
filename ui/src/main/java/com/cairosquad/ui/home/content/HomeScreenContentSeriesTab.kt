package com.cairosquad.ui.home.content

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.MediaHorizontalPager
import com.cairosquad.ui.movio_component.MediaHorizontalPagerItem
import com.cairosquad.ui.movio_component.MediaSection
import com.cairosquad.ui.movio_component.MediaSectionItem
import com.cairosquad.ui.movio_component.MediaSectionLayoutType
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState

@Composable
fun HomeScreenContentSeriesTab(
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
            mediaList = screenState.topRatingSeries
                .map(MediaHorizontalPagerItem::fromHomeSeriesUiState)
                .take(7),
            initialPage = 3,
            onClickMedia = listener::onClickMovie
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.topRatingSeries.map(MediaSectionItem::fromHomeSeriesUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.top_rating),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyRow,
            seeAllAction = { listener.onClickSeeAllTopRated(true) }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.airingTodaySeries.map(MediaSectionItem::fromHomeSeriesUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.airing_today),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyRow,
            seeAllAction = { listener.onClickSeeAllAiringToday() }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.onTvSeries.map(MediaSectionItem::fromHomeSeriesUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.on_tv),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyRow,
            seeAllAction = { listener.onClickSeeAllOnTv() }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.moreRecommendedSeries
                .map(MediaSectionItem::fromHomeSeriesUiState)
                .take(8),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.more_recommended),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyVerticalGrid(158),
            seeAllAction = { listener.onClickSeeAllMoreRecommended(false) }
        )
    }
}