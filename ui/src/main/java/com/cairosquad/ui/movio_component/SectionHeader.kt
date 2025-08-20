package com.cairosquad.ui.movio_component

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
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
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    actionIcon: ImageVector? = null,
    onActionClick: () -> Unit = {}
) {
    val layoutDirection = LocalLayoutDirection.current
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
                .clickable(onClick = onActionClick),
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
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .scale(
                            scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f,
                            scaleY = 1f
                        ),
                    imageVector = ImageVector.vectorResource(R.drawable.arrow),
                    contentDescription = "See All Icon",
                    tint = Theme.color.surfaces.onSurfaceVariant
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

