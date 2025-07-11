package com.cairosquad.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun RecentSearchItem(
    modifier: Modifier = Modifier,
    recentItem: String,
    query: String,
    onRecentSearchItemClicked: (String) -> Unit,
    onRemoveHistoryItem: (String) -> Unit
) {
    val recentItemText = buildAnnotatedString {
        val startIndex = recentItem.indexOf(query)

        if (startIndex == -1 || query.isBlank()) {
            withStyle(style = SpanStyle(color = Theme.color.surfaces.onSurfaceVariant)) {
                append(recentItem)
            }
        } else {
            val endIndex = startIndex + query.length

            if (startIndex > 0) {
                withStyle(style = SpanStyle(color = Theme.color.surfaces.onSurfaceVariant)) {
                    append(recentItem.substring(0, startIndex))
                }
            }

            withStyle(style = SpanStyle(color = Theme.color.surfaces.onSurface)) {
                append(recentItem.substring(startIndex, endIndex))
            }

            if (endIndex < recentItem.length) {
                withStyle(style = SpanStyle(color = Theme.color.surfaces.onSurfaceVariant)) {
                    append(recentItem.substring(endIndex))
                }
            }
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .padding(vertical = 8.dp)
                .clickable(onClick = { onRecentSearchItemClicked(recentItem) }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.recent),
                contentDescription = stringResource(R.string.recent_item_icon),
                colorFilter = ColorFilter.tint(Theme.color.surfaces.onSurfaceVariant)
            )
            BasicText(
                modifier = Modifier.weight(1f),
                text = recentItemText,
                style = Theme.textStyle.label.smallRegular14
            )
        }
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { onRemoveHistoryItem(recentItem) })
                .size(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_close),
            contentDescription = stringResource(R.string.clear_search_result),
            colorFilter = ColorFilter.tint(Theme.color.surfaces.onSurfaceVariant)
        )
    }
}

@Preview
@Composable
private fun RecentSearchItemPreview() {
    MovioTheme {
        RecentSearchItem(
            recentItem = "Ana De Armas",
            query = "Ana",
            onRecentSearchItemClicked = {},
            onRemoveHistoryItem = {}
        )
    }
}