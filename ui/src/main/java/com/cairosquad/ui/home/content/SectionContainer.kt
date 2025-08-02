package com.cairosquad.ui.home.content

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun SectionContainer(
    listState: LazyListState,
    index: Int,
    onVisible: () -> Unit,
    content: @Composable () -> Unit
) {
    val layoutInfo = listState.layoutInfo
    val isVisible = remember(layoutInfo) {
        derivedStateOf {
            layoutInfo.visibleItemsInfo.any { it.index == index }
        }
    }

    var triggered by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible.value) {
        if (isVisible.value && !triggered) {
            triggered = true
            onVisible()
        }
    }

    content()
}