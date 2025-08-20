package com.cairosquad.design_system.basic_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackButtonClicked: (() -> Unit)? = null,
    onShareButtonClicked: (() -> Unit)? = null,
    onFavoriteButtonClicked: (() -> Unit)? = null,
    isFavorite: Boolean = false,
) {
    val layoutDirection = LocalLayoutDirection.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        if (onBackButtonClicked != null) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onBackButtonClicked)
                    .size(40.dp)
                    .padding(8.dp)
                    .graphicsLayer {
                        if (layoutDirection == LayoutDirection.Rtl) {
                            scaleX = -1f
                        }
                    },
                painter = painterResource(R.drawable.arrow_left_icon_round),
                contentDescription = stringResource(R.string.back_icon),
                tint = Theme.color.surfaces.onSurface,
            )
        }

        val titleHorizontalPadding =
            if (onShareButtonClicked != null && onFavoriteButtonClicked != null) 80
            else if (
                onShareButtonClicked != null
                || onFavoriteButtonClicked != null
                || onBackButtonClicked != null
            ) 40
            else 0

        if (title != null) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = titleHorizontalPadding.dp),
                text = title,
                color = Theme.color.surfaces.onSurface,
                style = Theme.textStyle.headline.largeBold16,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            if (onShareButtonClicked != null) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onShareButtonClicked)
                        .size(40.dp)
                        .padding(8.dp),
                    painter = painterResource(R.drawable.share_icon_round),
                    contentDescription = stringResource(R.string.share_icon),
                    tint = Theme.color.surfaces.onSurface,
                )
            }

            if (onFavoriteButtonClicked != null) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onFavoriteButtonClicked)
                        .size(40.dp)
                        .padding(8.dp),
                    painter = if (!isFavorite) painterResource(R.drawable.heart_icon_round) else painterResource(
                        R.drawable.heart_icon_round_fill
                    ),
                    contentDescription = stringResource(R.string.favorite_icon),
                    tint = if (!isFavorite) Theme.color.surfaces.onSurface else Theme.color.system.onErrorContainer,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovioAppBarPreview() {
    MovioTheme {
        Column(
            Modifier.background(Theme.color.surfaces.surface),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            AppBar(
                title = "Title",
                onBackButtonClicked = null,
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            AppBar(
                title = "Title",
                onBackButtonClicked = {},
                onShareButtonClicked = null,
                onFavoriteButtonClicked = {}
            )
            AppBar(
                title = "Title",
                onBackButtonClicked = null,
                onShareButtonClicked = {},
                onFavoriteButtonClicked = null
            )
            AppBar(
                title = "Title",
                onBackButtonClicked = {},
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            AppBar(
                title = "Title",
                onBackButtonClicked = {},
                onShareButtonClicked = {},
                onFavoriteButtonClicked = {},
                isFavorite = true
            )
        }
    }
}