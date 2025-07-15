package com.cairosquad.design_system.color

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val darkThemeColors = MovioColors(
    brand = Brand(
        primary = Color(0xFF724CF8),
        onPrimary = Color(0xFFEBE6FE),
        primaryContainer = Color(0xFF181D32),
        onPrimaryContainer = Color(0xFFEFEFF1),
    ), surfaces = Surfaces(
        surface = Color(0xFF080D24),
        onSurface = Color(0xFFF0F5FF),
        surfaceContainer = Color(0xFF1A162F),
        onSurfaceContainer = Color(0xFFAEB3CC),
        surfaceVariant = Color(0xFF232940),
        onSurfaceVariant = Color(0xFF999DB3),
        outline = Color(0xFF434246),
        outlineVariant = Color(0xFF2F2E34),
        onSurfaceAt1 = Color(0xDEFFFFFF),
        onSurfaceAt2 = Color(0x61FFFFFF),
        onSurfaceAt3 = Color(0x1FFFFFFF),
        onSurfaceAt4 = Color(0x991A162F)
    ), system = System(
        error = Color(0xFF2A1010),
        onError = Color(0xFFFFDEDF),
        errorContainer = Color(0xFFEE7277),
        onErrorContainer = Color(0xFFE53935),
        warning = Color(0xFFDDBD2D),
        onWarning = Color(0xFFFFFEF9),
        onWarningContainer = Color(0xFFC6BFA2),
        success = Color(0xFF2C922A),
        onSuccess = Color(0xFFF6FFF6),
        successContainer = Color(0xFFE7FFE6),
        onSuccessContainer = Color(0xFF136912),
        dropShadow = Color(0x0FFFFFFF),
        defaultImageBackground = Color(0xFF252E3C)
    ),
    Gradiant(
        Brush.horizontalGradient(
            colors = listOf(
                Color(0x33EBE6FE),
                Color(0xFFF5F3FF),
                Color(0x33FFFFFF)
            )
        ),
        shimmerGradientColors = listOf(
            Color.Transparent,
            Color(0xFF080D24),
            Color.Transparent,
        ),
        primaryGradient = Brush.horizontalGradient(
            colors = listOf(Color(0xFFB7A4FB), Color(0xFF724CF8))
        )
    )
)