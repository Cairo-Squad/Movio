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
            mediaList = screenState.topRatingMovies
                .map(MediaHorizontalPagerItem::fromHomeMovieUiState)
                .take(7),
            initialPage = 3,
            onClickMedia = listener::onClickMovie
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.topRatingMovies.map(MediaSectionItem::fromHomeMovieUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.top_rating),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyRow,
            seeAllAction = { }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.trendingMovies.map(MediaSectionItem::fromHomeMovieUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.trending),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyHorizontalGrid(3),
            seeAllAction = { }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.freeToWatchMovies.map(MediaSectionItem::fromHomeMovieUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.free_to_watch),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyRow,
            seeAllAction = { }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.upcomingMovies.map(MediaSectionItem::fromHomeMovieUiState),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.up_coming),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyRow,
            seeAllAction = { }
        )

        MediaSection(
            modifier = Modifier.padding(bottom = 32.dp),
            mediaList = screenState.moreRecommendedMovies
                .map(MediaSectionItem::fromHomeMovieUiState)
                .take(8),
            onClickMedia = listener::onClickMovie,
            sectionTitle = stringResource(R.string.more_recommended),
            mediaSectionLayoutType = MediaSectionLayoutType.LazyVerticalGrid(158),
            seeAllAction = { }
        )
    }
}