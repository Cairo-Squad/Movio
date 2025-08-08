package com.cairosquad.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    trailingText: String? = null,
    trailingIcon: Painter? = null,
    onClick: () -> Unit
) {
    val layoutDirections = LocalLayoutDirection.current
    Row(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            contentDescription = icon.toString(),
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 16.dp, end = 8.dp)
                .size(24.dp),
            tint = Theme.color.surfaces.onSurface
        )
        Text(
            text = title,
            style = Theme.textStyle.title.mediumMedium16,
            color = Theme.color.surfaces.onSurface
        )
        Spacer(Modifier.weight(1f))
        if (trailingText != null) {
            Text(
                text = trailingText,
                style = Theme.textStyle.label.smallRegular14,
                color = Theme.color.surfaces.onSurfaceVariant,
                modifier = Modifier
                    .padding(vertical = 15.5.dp)
                    .padding(end = 4.dp)
            )
        }
        if (trailingIcon != null) {
            Icon(
                painter = trailingIcon,
                contentDescription = trailingIcon.toString(),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(end = 16.dp)
                    .size(16.dp)
                    .graphicsLayer {
                        if (layoutDirections == LayoutDirection.Rtl) {
                            scaleX = -1f
                        }
                    },
                tint = Theme.color.surfaces.onSurfaceVariant
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true , locale = "ar")
@Composable
private fun SettingsItemPreview() {
    MovioTheme {
        SettingsItem(
            icon = painterResource(id = com.cairosquad.ui.R.drawable.logout),
            title = "Logout",
            trailingText = "John Doe",
            trailingIcon = painterResource(id = R.drawable.arrow),
            onClick = {}
        )
    }
}