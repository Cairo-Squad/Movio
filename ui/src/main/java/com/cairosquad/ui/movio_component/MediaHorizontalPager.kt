package com.cairosquad.ui.movio_component

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MediaHorizontalPager(
    mediaList: List<MediaHorizontalPagerItem>,
    initialPage: Int,
    onClickMedia: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { mediaList.size }
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % mediaList.size,
                animationSpec = tween(600)
            )
        }
    }

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Box(
        modifier
            .height(430.dp)
            .fillMaxWidth()
    ) {
        mediaList.forEachIndexed { pageIndex, media ->
            val pageOffset =
                (pageIndex - pagerState.currentPage - pagerState.currentPageOffsetFraction)
            val isCurrentPageFloat = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            SafeImageViewer(
                modifier = Modifier
                    .alpha(isCurrentPageFloat)
                    .fillMaxSize()
                    .blur(20.dp)
                    .offset(y = (-28).dp),
                model = media.photoPath,
                contentDescription = stringResource(R.string.movie_poster),
                loadingPlaceholder = { },
                nonNudeThreshold = 0.0
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 132.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 2,
                contentPadding = PaddingValues(horizontal = ((screenWidthDp - 200) / 2).dp),
                pageSpacing = (-50).dp
            ) { pageIndex ->

                val media = mediaList[pageIndex]
                val pageOffset =
                    (pageIndex - pagerState.currentPage - pagerState.currentPageOffsetFraction)

                val isCurrentPageFloat = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)

                val cardAngle = 20.0f * pageOffset
                val cardSize = DpSize(
                    width = lerp(140, 200, isCurrentPageFloat).dp,
                    height = lerp(200, 260, isCurrentPageFloat).dp
                )

                Box(
                    modifier = Modifier
                        .size(200.dp, 260.dp)
                        .zIndex(isCurrentPageFloat),
                    contentAlignment = Alignment.Center
                ) {
                    MediaHorizontalPagerCard(
                        modifier = Modifier
                            .size(cardSize)
                            .rotate(cardAngle),
                        title = media.title,
                        imgUrl = media.photoPath,
                        genres = media.genres,
                        isCurrentPageFloat = isCurrentPageFloat,
                        onClick = { onClickMedia(media.id) }
                    )
                }
            }

            PageIndication(
                selectedIndex = pagerState.currentPage,
                pageCount = mediaList.size,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Black.copy(alpha = 0f)
                        ),
                    )
                )
                .statusBarsPadding()
                .padding(top = 48.dp)
                .fillMaxWidth()
        )
    }
}

data class MediaHorizontalPagerItem(
    val id: Long,
    val title: String,
    val photoPath: String,
    val genres: List<String>,
) {
    companion object {
        val fakeMediaItems = listOf(
            MediaHorizontalPagerItem(
                id = 1311031,
                title = "Demon Slayer: Kimetsu no Yaiba Infinity Castle",
                photoPath = "https://image.tmdb.org/t/p/w500/aFRDH3P7TX61FVGpaLhKr6QiOC1.jpg",
                genres = listOf("Animation", "Action", "Fantasy", "Thriller")
            ),
            MediaHorizontalPagerItem(
                id = 1061474,
                title = "Superman",
                photoPath = "https://image.tmdb.org/t/p/w500/ombsmhYUqR4qqOLOxAyr5V8hbyv.jpg",
                genres = listOf("Science Fiction", "Adventure", "Action")
            ),
            MediaHorizontalPagerItem(
                id = 541671,
                title = "Ballerina",
                photoPath = "https://image.tmdb.org/t/p/w500/2VUmvqsHb6cEtdfscEA6fqqVzLg.jpg",
                genres = listOf("Action", "Thriller", "Crime")
            ),
            MediaHorizontalPagerItem(
                id = 1087192,
                title = "How to Train Your Dragon",
                photoPath = "https://image.tmdb.org/t/p/w500/q5pXRYTycaeW6dEgsCrd4mYPmxM.jpg",
                genres = listOf("Fantasy", "Family", "Action")
            ),
            MediaHorizontalPagerItem(
                id = 1269208,
                title = "Wall to Wall",
                photoPath = "https://image.tmdb.org/t/p/w500/5hlNv3Kd9xovvSgrslWhMriGpZ8.jpg",
                genres = listOf("Thriller", "Drama")
            ),
            MediaHorizontalPagerItem(
                id = 617126,
                title = "The Fantastic Four: First Steps",
                photoPath = "https://image.tmdb.org/t/p/w500/x26MtUlwtWD26d0G0FXcppxCJio.jpg",
                genres = listOf("Science Fiction", "Adventure")
            ),
            MediaHorizontalPagerItem(
                id = 1071585,
                title = "M3GAN 2.0",
                photoPath = "https://image.tmdb.org/t/p/w500/4a63rQqIDTrYNdcnTXdPsQyxVLo.jpg",
                genres = listOf("Action", "Science Fiction", "Thriller")
            )
        )
    }
}

@Composable
private fun MediaHorizontalPagerCard(
    title: String,
    imgUrl: String,
    genres: List<String>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isCurrentPageFloat: Float = 1f
) {
    Box(
        modifier
            .clip(RoundedCornerShape(8.dp))
    ) {
        SafeImageViewer(
            modifier = Modifier.fillMaxSize(),
            model = imgUrl,
            contentDescription = stringResource(R.string.movie_poster),
            loadingPlaceholder = { LoadingMovieImage(Modifier.fillMaxSize()) },
        )
        Icon(
            modifier = Modifier
                .alpha(lerp(0f, 1f, isCurrentPageFloat))
                .align(Alignment.Center)
                .size(40.dp)
                .background(Theme.color.surfaces.onSurfaceAt2, CircleShape)
                .padding(12.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_play),
            contentDescription = stringResource(R.string.play),
            tint = Theme.color.brand.onPrimary,
        )
        var bottomSectionDp by remember { mutableIntStateOf(20) }
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .alpha(lerp(0f, 1f, isCurrentPageFloat))
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .onGloballyPositioned {
                    bottomSectionDp = with(density) { it.size.height.toDp().value.toInt() }
                },
        ) {
            SafeImageViewer(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(bottomSectionDp.dp)
                    .blur(20.dp),
                alignment = Alignment.BottomCenter,
                model = imgUrl,
                contentDescription = stringResource(R.string.movie_poster),
                loadingPlaceholder = { },
                nonNudeThreshold = 0.0
            )
            Column(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BasicText(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Theme.textStyle.label.mediumMedium12
                        .copy(Theme.color.brand.onPrimary)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    (0..< minOf(3, genres.size)).forEach {
                        item {
                            ChipWithNoBackGround(text = genres[it])
                        }
                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .background(Color.Black.copy(alpha = lerp(0.80f, 0f, isCurrentPageFloat)))
        )
    }
}

@Composable
private fun ChipWithNoBackGround(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = Theme.color.surfaces.onSurfaceAt3,
                shape = CircleShape
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = Theme.textStyle.label.mediumMedium12
            .copy(Theme.color.brand.onPrimary)
    )
}

@Composable
private fun PageIndication(
    selectedIndex: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (0..<pageCount).forEach { pageIndex ->

            val width by animateDpAsState(
                targetValue = if (selectedIndex == pageIndex) 15.dp else 5.dp,
                animationSpec = tween(500)
            )
            val color by animateColorAsState(
                targetValue =
                    if (selectedIndex == pageIndex) Color.White.copy(alpha = .87f)
                    else Color.White.copy(alpha = 0.38f),
                animationSpec = tween(500)
            )

            Box(
                modifier = Modifier
                    .size(
                        width = width,
                        height = 5.dp
                    )
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}