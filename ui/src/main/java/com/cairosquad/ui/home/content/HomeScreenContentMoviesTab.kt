package com.cairosquad.ui.home.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.cairosquad.viewmodel.home.HomeViewModel
import com.cairosquad.viewmodel.home.getSectionUiStateByContentType
import com.cairosquad.viewmodel.util.MediaType.MOVIES

@Composable
fun HomeScreenContentMoviesTab(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        item {
            MediaHorizontalPager(
                modifier = Modifier,
                mediaList = screenState.popularMovies
                    .map(MediaHorizontalPagerItem::fromHomeMediaUiState)
                    .take(HomeViewModel.HORIZONTAL_PAGER_COUNT),
                initialPage = HomeViewModel.HORIZONTAL_PAGER_COUNT / 2,
                onClickMedia = listener::onMediaClick
            )
        }

        itemsIndexed(HomeViewModel.homePageMoviesSections) { sectionIndex, mediaContentType ->
            val mediaList = remember(screenState.movieSections) {
                screenState
                    .movieSections
                    .getSectionUiStateByContentType(mediaContentType)
                    .map(MediaSectionItem::fromHomeMediaUiState)
            }
            MediaSection(
                modifier = Modifier.padding(bottom = 32.dp),
                mediaList = mediaList,
                sectionTitle = stringResource(mediaContentType.titleId),
                mediaSectionLayoutType = getMediaSectionLayout(mediaContentType),
                onClickMedia = listener::onMediaClick,
                seeAllAction = { listener.onSeeAllClick(mediaContentType, MOVIES) }
            )
        }
    }
}