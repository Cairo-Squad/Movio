package com.cairosquad.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    actionIcon: ImageVector? = null,
    onActionClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.title.mediumMedium16,
            modifier = Modifier.weight(1f),
            color = Theme.color.surfaces.onSurface
        )

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onActionClick)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (actionText != null) {
                Text(
                    text = actionText,
                    style = Theme.textStyle.label.smallRegular14,
                    color = Theme.color.surfaces.onSurfaceVariant
                )
            }
            if (actionIcon != null) {
                Image(
                    modifier = Modifier
                        .size(16.dp),
                    imageVector = actionIcon,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Theme.color.surfaces.onSurfaceVariant)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    val arrowIcon = ImageVector.vectorResource(id = R.drawable.arrow)

    MovioTheme {
        Column {
            SectionHeader(
                title = "For you",
                actionText = "See all",
                actionIcon = arrowIcon,
                onActionClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = "Explore more", onActionClick = {})
        }
    }
}

