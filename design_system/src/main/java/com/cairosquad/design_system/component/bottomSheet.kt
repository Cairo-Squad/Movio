package com.cairosquad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
/**
 * Displays a customizable modal bottom sheet.
 *
 * @param isVisible Controls the visibility of the bottom sheet.
 * @param onDismiss Callback invoked when clicking outside the bottom sheet.
 * @param modifier Modifier to be applied to the bottom sheet.
 * @param containerColor Background color of the bottom sheet, equal to surface by default .
 * @param content Composable content to be displayed inside the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Theme.color.surfaces.surface,
    content: @Composable () -> Unit
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
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetContentPreview() {

    var isVisible = remember { mutableStateOf(false) }

    MovioTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surfaces.surface),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Theme.color.brand.primary, CircleShape)
                    .clickable { isVisible.value = true }
                    .padding(12.dp),

            contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Show Bottom Sheet",
                    modifier = Modifier.align(Alignment.Center),
                    style = Theme.textStyle.label.smallRegular12,
                    color = Theme.color.surfaces.onSurface
                )
            }
            BottomSheet(
                isVisible = isVisible.value,
                onDismiss = { isVisible.value = false },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Custom Bottom Sheet Content",
                        style = Theme.textStyle.title.largeBold16,
                        color = Theme.color.surfaces.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .background(Theme.color.brand.primary, CircleShape)
                            .clickable { isVisible.value = false }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "hide Bottom Sheet",
                            modifier = Modifier.align(Alignment.Center),
                            style = Theme.textStyle.label.smallRegular12,
                            color = Theme.color.surfaces.onSurface
                        )
                    }
                }
            }
        }
    }
}