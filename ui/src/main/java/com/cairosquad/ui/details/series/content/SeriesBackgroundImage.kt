package com.cairosquad.ui.details.series.content

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState


@Composable
fun SeriesBackgroundImage(state: SeriesDetailsScreenState) {
    when (state.basicDetailsSectionState) {
        SeriesDetailsScreenState.SectionStatus.LOADING -> {}
        SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
            if (state.series.posterPath.isNotEmpty()) {
                Box {
                    SafeImageViewer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .then(
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    Modifier.blur(
                                        16.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .offset(y = (-28).dp),
                        model = BuildConfig.IMAGE_BASE_URL + state.series.posterPath,
                        contentDescription = "",
                        blur = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 16 else 0,
                        isBlurForced = true
                    )
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    brush = verticalGradient(
                                        colors = listOf(
                                            Theme.color.surfaces.surface.copy(alpha = 0.35f),
                                            Theme.color.surfaces.surface.copy(alpha = 0.50f),
                                            Theme.color.surfaces.surface.copy(alpha = 0.90f),
                                            Theme.color.surfaces.surface,
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }

        SeriesDetailsScreenState.SectionStatus.ERROR -> {}
    }
}