package com.cairosquad.ui.details.episodes.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme

@Composable
fun BoxScope.BottomFadingGradient() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
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