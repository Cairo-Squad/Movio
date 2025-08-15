package com.cairosquad.ui.movio_component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.Theme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val swipeOffset = remember { Animatable(0f) }
    val maxSwipeDistance = 200f
    val cardWidth = remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl


    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                cardWidth.floatValue = it.size.width.toFloat()
            }
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .align( Alignment.CenterEnd)
                .width(
                    with(LocalDensity.current) {
                        if (!isRtl) {
                            (-swipeOffset.value).coerceAtLeast(0f).toDp()
                        } else {
                            swipeOffset.value.coerceAtLeast(0f).toDp()
                        }
                    }
                )
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            DeleteComponent(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(if(!isRtl)swipeOffset.value.roundToInt() else -swipeOffset.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val (min, max) = if (!isRtl) {
                            -maxSwipeDistance to 0f
                        } else {
                            0f to maxSwipeDistance
                        }

                        val newOffset = (swipeOffset.value + delta).coerceIn(min, max)
                        coroutineScope.launch {
                            swipeOffset.snapTo(newOffset)
                        }
                    },
                    onDragStopped = {
                        val shouldDelete = if (!isRtl) {
                            swipeOffset.value < -cardWidth.floatValue * 0.075f
                        } else {
                            swipeOffset.value > cardWidth.floatValue * 0.075f
                        }

                        coroutineScope.launch {
                            if (shouldDelete) {
                                swipeOffset.animateTo(if (!isRtl) -maxSwipeDistance else maxSwipeDistance)
                                onDelete()
                            } else {
                                swipeOffset.animateTo(0f)
                            }
                        }
                    }
                )
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
private fun DeleteComponent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                Theme.color.brand.primary,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(com.cairosquad.ui.R.drawable.trash),
            contentDescription = "Trash Icon",
            tint = Theme.color.brand.onPrimary
        )
    }
}