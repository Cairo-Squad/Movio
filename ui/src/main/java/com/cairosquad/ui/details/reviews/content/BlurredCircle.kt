package com.cairosquad.ui.details.reviews.content

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme

@Composable
fun BoxScope.BlurredCircle() {
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .size(230.dp)
            .align(Alignment.TopEnd)
            .then(
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    Modifier.dropShadow(
                        shape = CircleShape,
                        color = Theme.color.surfaces.onSurfaceAt5,
                        blur = 264.dp,
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        alpha = 0.10f
                    )
                } else {
                    Modifier
                        .blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .background(
                            color = Theme.color.surfaces.onSurfaceAt5,
                            shape = CircleShape
                        )
                }
            )
    )
}