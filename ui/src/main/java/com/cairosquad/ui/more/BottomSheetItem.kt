package com.cairosquad.ui.more

import androidx.compose.ui.graphics.painter.Painter
import com.cairosquad.viewmodel.more.MoreScreenState

data class ThemeBottomSheetItem(
    val icon: Painter,
    val text: String,
    val theme : MoreScreenState.Theme,
)
