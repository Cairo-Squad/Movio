package com.cairosquad.design_system.basic_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Theme.color.surfaces.surface,
    scrimColor: Color = Theme.color.surfaces.onSurfaceAt4,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var modifiedIsVisible by remember { mutableStateOf(isVisible) }
    LaunchedEffect(isVisible) {
        if (isVisible) {
            modifiedIsVisible = true
        } else {
            sheetState.hide()
            modifiedIsVisible = false
        }
    }

    if (modifiedIsVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier,
            sheetState = sheetState,
            containerColor = containerColor,
            scrimColor = scrimColor,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 16.dp)
                        .size(36.dp, 4.dp)
                        .background(Theme.color.surfaces.surfaceVariant, CircleShape)
                )
            }
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun BottomSheetLightPreview() {
    BottomSheetPreview(isDarkTheme = false)
}

@Preview
@Composable
private fun BottomSheetDarkPreview() {
    BottomSheetPreview(isDarkTheme = true)
}

@Composable
private fun BottomSheetPreview(isDarkTheme: Boolean = false) {

    val isVisible = remember { mutableStateOf(false) }

    MovioTheme(
        isDarkTheme = isDarkTheme
    ) {
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

            BottomSheet(
                isVisible = isVisible.value,
                onDismiss = { isVisible.value = false }
            ) {
                Text(
                    text = "Custom Bottom Sheet Content",
                    style = Theme.textStyle.title.largeBold16,
                    color = Theme.color.surfaces.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp)
                )
                Text(
                    text = "Show Bottom Sheet",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp)
                        .background(Theme.color.brand.primary, CircleShape)
                        .clip(CircleShape)
                        .clickable { isVisible.value = false }
                        .padding(12.dp),
                    style = Theme.textStyle.label.smallRegular12,
                    color = Theme.color.brand.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}