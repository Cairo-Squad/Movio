package com.cairosquad.ui.movio_component.bottom_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun ShareBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onCopyLinkClick: () -> Unit,
    onShareFacebookClick: () -> Unit,
    onShareXClick: () -> Unit
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BasicText(
                text = stringResource(com.cairosquad.ui.R.string.share_via),
                style = Theme.textStyle.body.mediumMedium14.copy(
                    color = Theme.color.surfaces.onSurface,
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                ShareItem(
                    shareIcon = painterResource(R.drawable.copy),
                    shareTitle = stringResource(R.string.copy_link),
                    onShareClick = onCopyLinkClick
                )
                ShareItem(
                    shareIcon = painterResource(R.drawable.facebook),
                    shareTitle = stringResource(R.string.facebook),
                    onShareClick = onShareFacebookClick
                )
                ShareItem(
                    shareIcon = painterResource(R.drawable.x),
                    shareTitle = stringResource(R.string.X),
                    onShareClick = onShareXClick
                )
            }
        }
    }
}

@Composable
private fun ShareItem(
    shareIcon: Painter,
    shareTitle: String,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 13.dp)
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onShareClick)
                .background(Theme.color.surfaces.surfaceContainer)
                .padding(12.dp),
            painter = shareIcon,
            contentDescription = "Share Icon",
            colorFilter = ColorFilter.tint(Theme.color.surfaces.onSurface)
        )
        BasicText(
            text = shareTitle,
            style = Theme.textStyle.label.smallRegular12.copy(
                color = Theme.color.surfaces.onSurface
            )
        )
    }
}

@MultiThemePreviews
@Composable
private fun ShareBottomSheetPreview(modifier: Modifier = Modifier) {
    var isVisible = remember { mutableStateOf(false) }

    MovioTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surfaces.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Show Bottom Sheet",
                modifier = Modifier
                    .background(Theme.color.brand.primary, CircleShape)
                    .clip(CircleShape)
                    .clickable { isVisible.value = true }
                    .padding(12.dp),
                style = Theme.textStyle.label.smallRegular12,
                color = Theme.color.brand.onPrimary,
                textAlign = TextAlign.Center
            )

            ShareBottomSheet(
                isVisible = isVisible.value,
                onDismiss = {
                    isVisible.value = false
                },
                {},
                {},
                {}
            )
        }
    }
}
