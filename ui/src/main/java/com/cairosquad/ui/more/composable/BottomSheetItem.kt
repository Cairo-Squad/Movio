package com.cairosquad.ui.more.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme

@Composable
fun BottomSheetItem(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    text: String,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) {
        Brush.linearGradient(
            listOf(
                Color(0xFFEBE6FE),
                Color(0xFF724CF8)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                Color.Transparent,
                Color.Transparent
            )
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                brush = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Theme.color.surfaces.surfaceContainer)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp), tint = Theme.color.surfaces.onSurface
            )
        }

        Text(
            text = text,
            style = Theme.textStyle.title.mediumMedium14,
            color = Theme.color.surfaces.onSurface,
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Spacer(Modifier.weight(1f))
        if (isSelected) {
            Image(
                painter = painterResource(
                    if (Theme.isDark) R.drawable.snack_bar_icon_success_tick_dark
                    else R.drawable.snack_bar_icon_success_tick_light
                ),
                contentDescription = stringResource(com.cairosquad.ui.R.string.success_check_mark),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Preview
@Composable
fun BottomSheetItemPreview() {
    BottomSheetItem(
        isSelected = false,
        icon = painterResource(com.cairosquad.ui.R.drawable.moon),
        text = "Dark Mode",
        onClick = {}
    )
}