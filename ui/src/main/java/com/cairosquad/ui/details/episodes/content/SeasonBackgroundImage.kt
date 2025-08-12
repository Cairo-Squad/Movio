package com.cairosquad.ui.details.episodes.content

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState

@Composable
fun SeasonBackgroundImage(uiState: EpisodesDetailsScreenState) {
    if (uiState.season.posterUrl.isNotEmpty()) {
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
                model = BuildConfig.IMAGE_BASE_URL + uiState.season.posterUrl,
                contentDescription = "",
                blur = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 16 else 0,
                isBlurForced = true
            )
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                BottomFadingGradient()
            }
        }
    }
}