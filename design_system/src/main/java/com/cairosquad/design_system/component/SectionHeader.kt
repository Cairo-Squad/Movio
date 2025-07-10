package com.cairosquad.design_system.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    actionIcon: Painter? = null,
    modifier: Modifier = Modifier
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

        if (actionText != null && actionIcon != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actionText,
                    style = Theme.textStyle.label.smallRegular14,
                    color = Theme.color.surfaces.onSurfaceVariant
                )

                Icon(
                    painter = actionIcon,
                    contentDescription = null,
                    tint = Theme.color.surfaces.onSurfaceVariant,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp)
                )
            }
        } else if (actionText != null) {
            Text(
                text = actionText,
                style = Theme.textStyle.label.smallRegular14,
                color = Theme.color.surfaces.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    val arrowIcon = painterResource(id = R.drawable.arrow_left_icon_round)

    MovioTheme {
        Column {
            SectionHeader(
                title = "For you",
                actionText = "See all",
                actionIcon = arrowIcon
            )

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = "Explore more")
        }
    }
}

