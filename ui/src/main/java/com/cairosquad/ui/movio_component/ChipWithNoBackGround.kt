package com.cairosquad.ui.movio_component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme

@Composable
fun ChipWithNoBackGround(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = Theme.color.surfaces.onSurfaceAt3,
                shape = CircleShape
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = Theme.textStyle.body.smallRegular10
            .copy(Theme.color.surfaces.onSurfaceContainer)
    )
}