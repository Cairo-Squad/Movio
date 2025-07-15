package com.cairosquad.ui.movio_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun InfoChip(
    text: String,
    imgRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                color = Theme.color.surfaces.surfaceContainer,
                shape = CircleShape
            )
            .border(
                width = 1.dp, color = Theme.color.surfaces.onSurfaceAt3,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(16.dp),
            painter = painterResource(id = imgRes),
            contentDescription = "Icon Info",
        )
        Text(
            modifier = Modifier
                .padding(bottom = 1.dp),
            text = text,
            color = Theme.color.surfaces.onSurfaceVariant,
            style = Theme.textStyle.label.smallRegular12,
        )
    }
}

@Preview
@Composable
private fun InfoChipStarPreview() {
    MovioTheme {
        InfoChip(text = "4.5", imgRes = R.drawable.star)
    }
}

@Preview
@Composable
private fun InfoChipTimePreview() {
    MovioTheme {
        InfoChip(text = "2h 5min", imgRes = R.drawable.time)
    }
}

@Preview
@Composable
private fun InfoChipDatePreview() {
    MovioTheme(isDarkTheme = true) {
        InfoChip(text = "06/06/2025", imgRes = R.drawable.date)
    }
}

@Preview
@Composable
private fun InfoChipPreview() {
    MovioTheme(isDarkTheme = true) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoChip(text = "4.5", imgRes = R.drawable.star)
            InfoChip(text = "2h 5min", imgRes = R.drawable.time)
            InfoChip(text = "06/06/2025", imgRes = R.drawable.date)
        }
    }
}