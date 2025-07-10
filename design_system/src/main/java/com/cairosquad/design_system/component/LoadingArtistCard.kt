package com.cairosquad.design_system.component

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun LoadingArtistCard(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = stringResource(R.string.loading_animation))

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
        end = Offset(shimmerEffectAnimation + 190f, shimmerEffectAnimation + 190f)
    )

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        Theme.color.surfaces.surfaceContainer,
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .height(15.dp)
                    .fillMaxWidth()
                    .background(Theme.color.surfaces.surfaceContainer, shape = CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .height(111.dp)
                .fillMaxWidth()
                .background(brush)
        )
    }
}

@MultiThemePreviews
@Composable
private fun LoadingArtistCardPreview() {
    MovioTheme {
        LazyVerticalGrid(
            modifier = Modifier.background(Theme.color.surfaces.surface),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(21) {
                LoadingArtistCard()
            }
        }
    }
}