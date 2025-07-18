package com.cairosquad.design_system.basic_component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.cairosquad.design_system.theme.Theme

@Composable
fun DesignSystemText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Theme.textStyle.body.smallRegular10,
    color: Color = Theme.color.surfaces.onSurface,
    maxLines: Int = 6,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        overflow = overflow
    )
}
