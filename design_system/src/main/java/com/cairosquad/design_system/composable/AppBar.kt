package com.cairosquad.design_system.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.design_system.R


@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackButtonClicked: (() -> Unit)? = null,
    onShareButtonClicked: (() -> Unit)? = null,
    onFavoriteButtonClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(
                elevation = 12.dp,
                ambientColor = Color(0x0FFFFFFF),
                spotColor = Color(0x0FFFFFFF)
            )
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        if (onBackButtonClicked != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onBackButtonClicked)
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp),
                    painter = painterResource(R.drawable.arrow_left_icon_round),
                    contentDescription = stringResource(R.string.back_icon),
                    tint = Theme.color.surfaces.onSurface,
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            if (title != null) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.headline.largeBold16,
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp))
        {
            if (onShareButtonClicked != null) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onShareButtonClicked)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp),
                        painter = painterResource(R.drawable.share_icon_round),
                        contentDescription = stringResource(R.string.share_icon),
                        tint = Theme.color.surfaces.onSurface,
                    )
                }
            }

            if (onFavoriteButtonClicked != null) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onFavoriteButtonClicked)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp),
                        painter = painterResource(R.drawable.heart_icon_round),
                        contentDescription = stringResource(R.string.favorite_icon),
                        tint = Theme.color.surfaces.onSurface,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovioAppBarPreview() {
    MovioTheme {
        Box(Modifier.background(Theme.color.surfaces.surface))
        {
            AppBar(
                title = "nour",
                onBackButtonClicked = {},
                onShareButtonClicked = {},
                onFavoriteButtonClicked = {})
        }
    }
}