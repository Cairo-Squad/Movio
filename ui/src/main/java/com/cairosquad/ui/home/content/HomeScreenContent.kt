package com.cairosquad.ui.home.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.AppBar
import com.cairosquad.ui.movio_component.MediaHorizontalPager
import com.cairosquad.ui.movio_component.MediaHorizontalPagerItem
import com.cairosquad.ui.movio_component.MediaSection
import com.cairosquad.ui.movio_component.MediaSectionItem
import com.cairosquad.ui.movio_component.MediaSectionLayoutType
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState

@Composable
fun HomeScreenContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
) {
    val listState = rememberScrollState()
    val density = LocalDensity.current

    val scrollThresholdPx = with(density) { 275.dp.toPx() }

    val progress = (listState.value / scrollThresholdPx).coerceIn(0f, 1f)

    val animatedEndColor = lerp(
        start = Color.Transparent,
        stop = Theme.color.surfaces.surface,
        fraction = progress
    )
    val animatedBrush = Brush.verticalGradient(
        colors = listOf(Theme.color.surfaces.surface, animatedEndColor)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(listState),
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(animatedBrush)
        ) {
            AppBar(modifier = Modifier.statusBarsPadding())

            val tabsList = remember {
                listOf(
                    R.string.all,
                    R.string.movies,
                    R.string.tv_shows,
                    R.string.categories,
                )
            }

            TabRow(
                modifier = Modifier,
                tabs = tabsList.map { stringResource(it) },
                selectedTabIndex = screenState.selectedTab.ordinal,
                onTabSelected = listener::onClickTab
            )
        }
    }
}