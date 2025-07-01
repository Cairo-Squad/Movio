package com.cairosquad.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.cairosquad.design_system.color.LocalMovioColor
import com.cairosquad.design_system.color.MovioColors
import com.cairosquad.design_system.text_style.LocalMovioTextStyle
import com.cairosquad.design_system.text_style.MovioTextStyle

object Theme {
    val color: MovioColors
        @Composable @ReadOnlyComposable get() = LocalMovioColor.current

    val textStyle: MovioTextStyle
        @Composable @ReadOnlyComposable get() = LocalMovioTextStyle.current
}