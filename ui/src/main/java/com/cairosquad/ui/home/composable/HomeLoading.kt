package com.cairosquad.ui.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import kotlin.math.absoluteValue

@Composable
fun HomeLoading(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollProgress: Float,
    modifier: Modifier = Modifier,
) {
    val tabsNamesResId = remember {
        listOf(
            com.cairosquad.ui.R.string.movies,
            com.cairosquad.ui.R.string.tv_shows,
            com.cairosquad.ui.R.string.categories,
        )
    }
    val pagerState = rememberPagerState(
        initialPage = 2,
        pageCount = { 5 }
    )
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val screenWidthDp = LocalConfiguration.current.screenWidthDp


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier.size(height = 28.dp, width = 25.dp),
                imageVector = ImageVector.vectorResource(R.drawable.logo),
                contentDescription = stringResource(R.string.movio)
            )
            BasicText(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                text = stringResource(R.string.movio),
                style = Theme.textStyle.display.largeBold18.copy(
                    brush = Theme.color.gradiant.logo
                )
            )
            LoadingMovieImage(
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
        }
        TabRow(
            modifier = Modifier,
            tabs = tabsNamesResId.map { stringResource(it) },
            selectedTabIndex = screenState.selectedTab.ordinal,
            onTabSelected = listener::onTabClick,
            scrollProgress = scrollProgress,
            tabColorWithScroll = Theme.color.brand.onPrimaryContainer,
            tabColorWithNoScroll = Theme.color.brand.onPrimary,
            indicatorColorWithScroll = Theme.color.gradiant.horizontalCategoriesGradient,
            indicatorColorWithNoScroll = Theme.color.gradiant.horizontalGradient
        )

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 2,
            contentPadding = PaddingValues(horizontal = ((screenWidthDp - 200) / 2).dp),
            pageSpacing = (-50).dp,
            reverseLayout = isRtl
        ) { pageIndex ->

            val pageOffset =
                (pageIndex - pagerState.currentPage - pagerState.currentPageOffsetFraction)

            val isCurrentPageFloat = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)

            val cardAngle = 20.0f * pageOffset
            val cardSize = DpSize(
                width = lerp(140, 200, isCurrentPageFloat).dp,
                height = lerp(200, 260, isCurrentPageFloat).dp
            )
            val imageOverlayColor = Theme.color.surfaces.horizontalImageOverlay.copy(
                (lerp(Theme.color.surfaces.horizontalImageOverlay.alpha, 0f, isCurrentPageFloat))
            )

            Box(
                modifier = Modifier
                    .size(200.dp, 260.dp)
                    .zIndex(-(pageOffset.absoluteValue)),
                contentAlignment = Alignment.Center
            ) {
                LoadingMovieImage(
                    modifier = Modifier
                        .size(cardSize)
                        .rotate(cardAngle)
                )
                Box(
                    modifier = Modifier
                        .size(cardSize)
                        .rotate(cardAngle)
                        .background(imageOverlayColor, RoundedCornerShape(8.dp))
                )
            }
        }
        repeat(4) {
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),

            ) {
                items(10) {
                    LoadingMovieCard(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width(124.dp),
                        height = 202.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeLoadingPreview() {
    MovioTheme(isDarkTheme = true) {
        HomeLoading(
            screenState = HomeScreenState(),
            listener = object : HomeInteractionsListener {
                override fun onProfileClick() {}

                override fun onMediaClick(mediaId: Long, isMovie: Boolean) {}

                override fun onSeeAllClick(
                    mediaContentType: MediaContentType,
                    mediaType: MediaType
                ) {
                }

                override fun onTabClick(tabIndex: Int) {}

                override fun onGenreSelected(genreIndex: Int) {}

                override fun onSortingSelected(filter: HomeScreenState.SortingType) {}

                override fun onRefresh() {}

            },
            scrollProgress = 0f,
        )

    }
}