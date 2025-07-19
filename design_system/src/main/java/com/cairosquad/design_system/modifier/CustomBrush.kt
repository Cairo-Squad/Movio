package com.cairosquad.design_system.modifier

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.CustomBrush(
    alpha: Float,
    blur: Dp
)=this.background(
    Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha),
            Color.Transparent,
        )
    )
).blur(blur)