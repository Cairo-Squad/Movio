package com.cairosquad.ui.movio_component

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.viewmodel.home.HomeScreenState
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MediaHorizontalPager(
	mediaList: List<MediaHorizontalPagerItem>,
	initialPage: Int,
	onClickMedia: (Long, Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val pagerState = rememberPagerState(
		initialPage = initialPage,
		pageCount = { mediaList.size }
	)

	var isAutoScrollingRightNow by remember { mutableStateOf(false) }

	LaunchedEffect(isAutoScrollingRightNow || pagerState.currentPageOffsetFraction.absoluteValue < 0.24f) {
		isAutoScrollingRightNow = false
		while (true) {
			delay(10_000)
			if (mediaList.isEmpty()) continue
			isAutoScrollingRightNow = true
			pagerState.animateScrollToPage(
				page = (pagerState.currentPage + 1) % mediaList.size,
				animationSpec = tween(600)
			)
			isAutoScrollingRightNow = false
		}
	}

	val layoutDirection = LocalLayoutDirection.current
	val isRtl = layoutDirection == LayoutDirection.Rtl

	val screenWidthDp = LocalConfiguration.current.screenWidthDp

	Box(modifier = modifier.fillMaxWidth()) {

		if (mediaList.size > pagerState.currentPage) {
			AnimatedContent(pagerState.currentPage) { pageIndex ->
				SafeImageViewer(
					modifier = Modifier
						.fillMaxWidth()
						.height(430.dp)
						.then(
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
								Modifier.blur(16.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
							} else {
								Modifier
							}
						)
						.offset(y = (- 28).dp),
					model = BuildConfig.IMAGE_BASE_URL + mediaList[pageIndex].photoPath,
					contentDescription = stringResource(R.string.movie_poster),
					blur = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 20 else 0,
					isBlurForced = true
				)
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(430.dp)
						.blur(20.dp)
						.offset(y = (-28).dp)
						.background(Theme.color.surfaces.overlay)
				)
			}
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)
					.align(Alignment.BottomCenter)
					.background(
						brush = verticalGradient(
							colors = listOf(
								Theme.color.surfaces.surface.copy(alpha = 0.00f),
								Theme.color.surfaces.surface.copy(alpha = 0.10f),
								Theme.color.surfaces.surface.copy(alpha = 0.50f),
								Theme.color.surfaces.surface.copy(alpha = 0.90f),
								Theme.color.surfaces.surface,
							)
						)
					)
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
				pageSpacing = (- 50).dp,
				reverseLayout = isRtl
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
						onClick = { onClickMedia(media.id, media.isMovie) }
					)
				}
			}

			PageIndication(
				selectedIndex = pagerState.currentPage,
				pageCount = mediaList.size,
				modifier = Modifier.padding(top = 16.dp)
			)
		}
	}
}

data class MediaHorizontalPagerItem(
	val id: Long,
	val title: String,
	val photoPath: String,
	val genres: List<String>,
	val isMovie: Boolean
) {

	companion object {

		fun fromHomeMediaUiState(media: HomeScreenState.MediaUiState): MediaHorizontalPagerItem {
			return MediaHorizontalPagerItem(
				id = media.id,
				title = media.title,
				photoPath = media.posterPath,
				genres = media.genres.map { it.name },
				isMovie = media.isMovie
			)
		}
	}
}