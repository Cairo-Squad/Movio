package com.cairosquad.ui.movio_component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndication(
    selectedIndex: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (0 ..< pageCount).forEach { pageIndex ->

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
                    .size(width = width, height = 5.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}