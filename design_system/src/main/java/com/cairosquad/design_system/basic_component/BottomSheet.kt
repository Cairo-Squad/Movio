package com.cairosquad.design_system.basic_component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import kotlinx.coroutines.delay

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
    val density = LocalDensity.current
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    var maxHeight by remember { mutableFloatStateOf(screenHeightDp * 0.95f) }
    var bottomSheetHeightDp by remember { mutableFloatStateOf(maxHeight) }
    var contentHeight by remember { mutableFloatStateOf(maxHeight - 40) }
    val animatedScrimColor by animateColorAsState(
        targetValue = if (isVisible) scrimColor else Color.Transparent,
        animationSpec = tween(500)
    )

    BackHandler(enabled = isVisible, onBack = onDismiss)

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(50)
            bottomSheetHeightDp = minOf(contentHeight + 40, maxHeight)
        } else {
            delay(500)
            bottomSheetHeightDp = maxHeight
            contentHeight = maxHeight - 40
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                other = if (isVisible) Modifier
                    .background(animatedScrimColor)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDismiss
                    )
                else Modifier
            )
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = isVisible,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = 500, delayMillis = 100
                ), initialOffsetY = { it }),
            exit = slideOutVertically(animationSpec = tween(500), targetOffsetY = { 2 * it }),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(bottomSheetHeightDp.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(containerColor)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { }),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { _, dragAmount ->
                                val displacement = with(density) { dragAmount.toDp().value }
                                val canBottomSheetExpand = bottomSheetHeightDp - contentHeight < 40
                                if (displacement < 0 && !canBottomSheetExpand) return@detectVerticalDragGestures
                                bottomSheetHeightDp = minOf(
                                    maxHeight, bottomSheetHeightDp - displacement
                                )
                                if (bottomSheetHeightDp < 150) onDismiss()
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 16.dp)
                            .size(36.dp, 4.dp)
                            .background(
                                color = Theme.color.surfaces.surfaceVariant, shape = CircleShape
                            )
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .onGloballyPositioned {
                            contentHeight = with(density) { it.size.height.toDp().value }
                        },
                    content = content
                )
            }
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
                textAlign = TextAlign.Center)

            BottomSheet(
                isVisible = isVisible.value, onDismiss = { isVisible.value = false }) {
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
                    textAlign = TextAlign.Center)
            }
        }
    }
}