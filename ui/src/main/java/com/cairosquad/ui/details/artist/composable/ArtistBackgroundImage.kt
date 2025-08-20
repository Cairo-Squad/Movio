package com.cairosquad.ui.details.artist.composable

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.modifier.CustomBrush
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistBackgroundImage(state: ArtistScreenState) {
    if (state.artist.photoPath.isBlank()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(335.dp)
                .offset(y = (-5).dp)
                .CustomBrush(0.5f, 16.dp),
        )
    } else {
        Box {
            SafeImageViewer(
                model = BuildConfig.IMAGE_BASE_URL + state.artist.photoPath,
                contentDescription = "blured image",
                modifier = Modifier
                    .fillMaxSize()
                    .height(335.dp)
                    .then(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Modifier.blur(16.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        } else {
                            Modifier
                        }
                    )
                    .offset(y = (-20).dp),
                blur = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 16 else 0,
                isBlurForced = true
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .blur(16.dp)
                        .background(Theme.color.surfaces.overlay)
                )
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .align(Alignment.BottomCenter)
                        .background(brush = Theme.color.gradiant.fadingGradient)
                )
            }
        }
    }
}