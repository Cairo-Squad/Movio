package com.cairosquad.design_system.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .padding(horizontal = 12.dp, vertical = 7.5.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            color = textColor,
            style = Theme.textStyle.label.smallRegular14,
        )
    }
}

@Preview
@Composable
private fun ChipPreview() {
    MovioTheme {
        Chip(title = "All", isSelected = true)
    }
}