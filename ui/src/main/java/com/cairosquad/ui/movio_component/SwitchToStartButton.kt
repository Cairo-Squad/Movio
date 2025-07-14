package com.cairosquad.ui.movio_component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StartSwipeButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.start_now),
    icon: Painter = painterResource(id = R.drawable.popcorn_ic),
    onSwipeComplete: () -> Unit
) {
    BoxWithConstraints {
        val totalWidth = maxWidth
        val initialWidth = 64.dp
        val scope = rememberCoroutineScope()
        val density = LocalDensity.current

        val initialOffsetPx = with(density) { initialWidth.toPx() }
        val maxOffsetPx = with(density) { (totalWidth - initialWidth).toPx() }

        val dragOffset = remember { mutableFloatStateOf(initialOffsetPx) }
        val isSwipeComplete = remember { mutableStateOf(false) }
        val finalWidth = remember { mutableFloatStateOf(0f) }

        val actualWidth = if (isSwipeComplete.value)
            initialOffsetPx + finalWidth.floatValue
        else
            dragOffset.floatValue

        val animatedWidth = animateFloatAsState(actualWidth, label = "")

        val progress = ((actualWidth - initialOffsetPx) / maxOffsetPx).coerceIn(0f, 1f)

        val purple = Theme.color.brand.primary
        val dark = Theme.color.surfaces.surfaceContainer

        Box(
            modifier = modifier
                .width(maxWidth)
                .height(64.dp)
                .clip(RoundedCornerShape(50))
                .background(dark)
                .then(
                    if (!isSwipeComplete.value) Modifier.pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, delta ->
                                dragOffset.floatValue = (dragOffset.floatValue + delta)
                                    .coerceIn(initialOffsetPx, initialOffsetPx + maxOffsetPx)
                            },
                            onDragEnd = {
                                val currentProgress =
                                    ((dragOffset.floatValue - initialOffsetPx) / maxOffsetPx).coerceIn(
                                        0f,
                                        1f
                                    )
                                if (currentProgress > 0.95f) {
                                    finalWidth.floatValue = maxOffsetPx
                                    isSwipeComplete.value = true
                                    onSwipeComplete()
                                } else {
                                    scope.launch {
                                        animate(
                                            initialValue = dragOffset.floatValue,
                                            targetValue = initialOffsetPx,
                                            animationSpec = tween(300)
                                        ) { value, _ ->
                                            dragOffset.floatValue = value
                                        }
                                    }
                                }
                            }
                        )
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            val shiftTrigger = 0.1f
            val maxShift = 180.dp

            val shiftAmount = animateDpAsState(
                targetValue = if (progress > shiftTrigger)
                    ((progress - shiftTrigger) / (1f - shiftTrigger) * maxShift.value).dp
                else 0.dp,
                animationSpec = tween(300)
            )

            AnimatedVisibility(
                visible = progress < .5f,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                Text(
                    text = text,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.title.mediumMedium16,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = shiftAmount.value)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = null,
                        tint = Theme.color.surfaces.onSurface.copy(
                            alpha = (progress - 0.3f + it * 0.2f).coerceIn(0f, 1f)
                        )
                    )
                }
            }

            val afterSwipeColorWidth = with(density) { animatedWidth.value.toDp() }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(afterSwipeColorWidth)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(50))
                    .background(purple)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = icon,
                        contentDescription = stringResource(id = R.string.icon),
                        modifier = Modifier.size(24.dp)
                    )
                    AnimatedVisibility(
                        visible = progress > 0.5f,
                        enter = fadeIn(animationSpec = tween(200)),
                        exit = fadeOut(animationSpec = tween(200))
                    ) {
                        Text(
                            text = text,
                            color = Theme.color.brand.onPrimary,
                            style = Theme.textStyle.title.mediumMedium16,
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_5")
@Composable
private fun Preview() {
    StartSwipeButton { }
}
