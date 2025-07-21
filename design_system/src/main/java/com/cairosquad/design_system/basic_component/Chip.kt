package com.cairosquad.design_system.basic_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun Chip(
    title: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    textStyle: TextStyle = Theme.textStyle.label.smallRegular14,
    onClick: () -> Unit = {},
) {
    val backgroundColor =
        if (isSelected) Theme.color.brand.primary else Theme.color.surfaces.onSurfaceAt3
    val textColor =
        if (isSelected) Theme.color.brand.onPrimary else Theme.color.surfaces.onSurfaceVariant
    Box(
        modifier = modifier
            .height(32.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            style = textStyle,
        )
    }
}

@Preview
@Composable
private fun ChipPreview1() {
    MovioTheme {
        Chip(title = "الكل", isSelected = true)
    }
}

@Preview
@Composable
private fun ChipPreview2() {
    MovioTheme {
        Chip(title = "All", isSelected = false)
    }
}