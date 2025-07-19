package com.cairosquad.ui.movio_component

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun LoadingReviewCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition =
        rememberInfiniteTransition(label = stringResource(R.string.loading_animation))

    val shimmerEffectAnimation by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = EaseInOut),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = Theme.color.gradiant.shimmerGradientColors,
        start = Offset(shimmerEffectAnimation, shimmerEffectAnimation),
        end = Offset(shimmerEffectAnimation + 200f, shimmerEffectAnimation + 200f)
    )

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Theme.color.surfaces.onSurfaceAt3,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .heightIn(min = 137.dp)
            .width(258.dp)
            .background(Theme.color.surfaces.surfaceContainer)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(brush)
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(0.3f)
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }

            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }

        Column(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}

@MultiThemePreviews
@Composable
private fun LoadingReviewCardPreview() {
    MovioTheme {
        LoadingReviewCard()
    }
}
