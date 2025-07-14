package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun Chip(
    title: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor =
        if (isSelected) Theme.color.brand.primary else Theme.color.surfaces.onSurfaceAt3
    val textColor =
        if (isSelected) Theme.color.brand.onPrimary else Theme.color.surfaces.onSurfaceVariant

    Text(
        modifier = modifier
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.5.dp),
        text = title,
        color = textColor,
        style = Theme.textStyle.label.smallRegular14,
    )
}

@Preview
@Composable
private fun ChipPreview1() {
    MovioTheme {
        Chip(title = "All", isSelected = true)
    }
}
@Preview
@Composable
private fun ChipPreview2() {
    MovioTheme {
        Chip(title = "All", isSelected = false)
    }
}