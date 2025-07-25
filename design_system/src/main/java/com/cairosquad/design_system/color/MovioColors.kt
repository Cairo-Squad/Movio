package com.cairosquad.design_system.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class MovioColors(
    val brand: Brand,
    val surfaces: Surfaces,
    val system: System,
    val gradiant: Gradiant,
)

data class Surfaces(
    val surface: Color,
    val onSurface: Color,
    val surfaceContainer: Color,
    val onSurfaceContainer: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val statusBarShadow: Color,
    val outlineVariant: Color,
    val onSurfaceAt1: Color,
    val onSurfaceAt2: Color,
    val onSurfaceAt3: Color,
    val onSurfaceAt4: Color,
    val onSurfaceAt5: Color,
)

data class Brand(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
)

data class System(
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val onWarningContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val dropShadow: Color,
    val defaultImageBackground: Color
)

data class Gradiant(
    val horizontalGradient: Brush,
    val shimmerGradientColors: List<Color>,
    val primaryGradient: Brush,
    val logo: Brush
)

internal val LocalMovioColor = staticCompositionLocalOf { lightThemeColors }