package com.cairosquad.ui.movio_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun LoginBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onLoginClick: () -> Unit
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(width = 60.dp, height = 66.dp)
                    .padding(16.dp),
                imageVector = ImageVector.vectorResource(R.drawable.logo),
                contentDescription = "Application Logo"
            )
            BasicText(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(R.string.you_don_t_have_an_account),
                style = Theme.textStyle.title.mediumMedium16.copy(
                    color = Theme.color.surfaces.onSurface,
                    textAlign = TextAlign.Center
                )
            )
            BasicText(
                modifier = Modifier.padding(bottom = 40.dp),
                text = stringResource(R.string.you_don_t_have_an_account_description),
                style = Theme.textStyle.label.smallRegular12.copy(
                    color = Theme.color.surfaces.onSurfaceContainer,
                    textAlign = TextAlign.Center
                )
            )
            Button(
                text = stringResource(R.string.login),
                onClick = onLoginClick
            )
        }
    }
}

@MultiThemePreviews
@Composable
private fun LoginBottomSheetPreview() {
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

            LoginBottomSheet(
                isVisible = isVisible.value,
                onDismiss = {
                    isVisible.value = false
                },
                {},
            )
        }
    }
}
