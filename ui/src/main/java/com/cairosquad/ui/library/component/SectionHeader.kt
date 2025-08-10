package com.cairosquad.ui.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun SectionHeader(
    sectionTitle: String,
    sectionIcon: ImageVector,
    onSectionClick: () -> Unit,
    modifier: Modifier = Modifier,
    sectionDescription: String? = null
) {
    val layoutDirection = LocalLayoutDirection.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = sectionIcon,
                contentDescription = "List Icon",
                tint = Theme.color.surfaces.onSurface
            )
            Column {
                Text(
                    text = sectionTitle,
                    style = Theme.textStyle.headline.mediumMedium18,
                    color = Theme.color.surfaces.onSurface
                )
                sectionDescription?.let {
                    Text(
                        text = it,
                        style = Theme.textStyle.label.smallRegular12,
                        color = Theme.color.surfaces.onSurfaceVariant
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onSectionClick)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(com.cairosquad.ui.R.string.view_all),
                style = Theme.textStyle.label.smallRegular14,
                color = Theme.color.surfaces.onSurfaceVariant
            )

            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .scale(
                        scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f,
                        scaleY = 1f
                    ),
                imageVector = ImageVector.vectorResource(R.drawable.arrow),
                contentDescription = "List Icon",
                tint = Theme.color.surfaces.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    MovioTheme {
        Box(Modifier.background(Theme.color.surfaces.surface)) {
            SectionHeader(
                sectionTitle = "Watchlist",
                sectionIcon = ImageVector.vectorResource(R.drawable.ic_list),
                onSectionClick = {},
                sectionDescription = "This list has empty"
            )
        }
    }
}