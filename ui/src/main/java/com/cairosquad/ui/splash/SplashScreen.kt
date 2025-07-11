package com.cairosquad.ui.splash

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val scaleLogoAnimation by animateFloatAsState(
        targetValue = if (!startAnimation) 0f else 1f,
        animationSpec = tween(1500, easing = EaseInOut)
    )
    val scaleBlurAnimation by animateFloatAsState(
        targetValue = if (!startAnimation) 0f else 1f,
        animationSpec = tween(1000, easing = EaseInOut, delayMillis = 500)
    )
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onNavigateNext()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize().alpha(0.3f),
            imageVector = ImageVector.vectorResource(R.drawable.grid),
            contentDescription = "Splash Screen Grid"
        )
        Box(
            modifier = Modifier.scale(scaleLogoAnimation),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .scale(scaleBlurAnimation)
                    .size(200.dp)
                    .blur(radius = 263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Color(0x33734EF8), CircleShape)
            )
            Column(
                modifier = Modifier.scale(scaleLogoAnimation),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    modifier = Modifier.size(width = 100.dp, height = 110.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.logo),
                    contentDescription = "Application Logo"
                )
                BasicText(
                    text = stringResource(R.string.movio),
                    style = Theme.textStyle.display.largeBold24.copy(
                        brush = Brush.verticalGradient(
                            colors = listOf<Color>(
                                Theme.color.brand.onPrimary,
                                Color(0xFF7C5DF6)
                            )
                        )
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    MovioTheme(isDarkTheme = true) {
        SplashScreen() {}
    }
}