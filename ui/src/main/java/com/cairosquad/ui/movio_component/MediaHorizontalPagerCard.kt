package com.cairosquad.ui.movio_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig

@Composable
fun MediaHorizontalPagerCard(
    title: String,
    imgUrl: String,
    genres: List<String>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isCurrentPageFloat: Float = 1f
) {

    var isImageSafe by remember { mutableStateOf(true) }

    Box(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .alpha(lerp(0.50f, 1f, isCurrentPageFloat))
    ) {
        SafeImageViewer(
            modifier = Modifier.fillMaxSize(),
            onIsImageSafeChanged = { isImageSafe = it },
            model = BuildConfig.IMAGE_BASE_URL + imgUrl,
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
                }
                .heightIn(min = 51.dp),
        ) {
            AnimatedVisibility(
                visible = isImageSafe,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                SafeImageViewer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottomSectionDp.dp),
                    alignment = Alignment.BottomCenter,
                    model = BuildConfig.IMAGE_BASE_URL + imgUrl,
                    contentDescription = stringResource(R.string.movie_poster),
                    loadingPlaceholder = { },
                    blur = 25,
                    isBlurForced = true
                )
            }
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
                        .copy(color = Theme.color.brand.onPrimary)
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    maxLines = 1
                ) {
                    genres.forEach { genre ->
                        ChipWithNoBackGround(text = genre)
                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .background(Color.Black.copy(alpha = lerp(0.30f, 0f, isCurrentPageFloat)))
        )
    }
}