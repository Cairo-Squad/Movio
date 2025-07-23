package com.cairosquad.design_system.basic_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
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
    isEnable: Boolean = true,
    onClick: () -> Unit = {}
) {
    val backgroundColor =
        if (isSelected) Theme.color.brand.primary else Theme.color.surfaces.onSurfaceAt3
    val textColor =
        if (isSelected) Theme.color.brand.onPrimary else Theme.color.surfaces.onSurfaceVariant
    val fontScale = LocalConfiguration.current.fontScale
    Box(
        modifier = modifier
            .height((32 * fontScale).dp)
            .clip(CircleShape)
            .clickable(onClick = onClick, enabled = isEnable)
            .background(color = backgroundColor, shape = CircleShape)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ChipPreview() {
    MovioTheme {
        Chip(title = "All", isSelected = false)
    }
}

@Preview
@Composable
private fun ChipPreview1() {
    MovioTheme {
        Chip(title = "All", isSelected = true)
    }
}

@Preview
@Preview(fontScale = 1.2f)
@Preview(fontScale = 1.5f)
@Preview(fontScale = 2f)
@Composable
private fun ChipPreview2() {
    MovioTheme {
        Column {
            val fontScale = LocalConfiguration.current.fontScale
            Chip(title = "fontScale: $fontScale", isSelected = false)
            Row(
                Modifier
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Chip(title = "الكل", isSelected = true)
                Chip(title = "All", isSelected = false)
                Chip(title = "Action", isSelected = true)
                Chip(title = "Documentary", isSelected = false)
            }
        }
    }
}