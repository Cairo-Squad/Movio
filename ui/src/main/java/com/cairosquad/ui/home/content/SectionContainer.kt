package com.cairosquad.ui.home.content

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.mapNotNull


@Composable
fun SectionContainer(
    listState: LazyListState,
    index: Int,
    onVisible: () -> Unit,
    content: @Composable () -> Unit
) {
    var triggered by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .mapNotNull { visibleItems ->
                if (!triggered && visibleItems.any { it.index == index }) true else null
            }
            .collect {
                triggered = true
                onVisible()
            }
    }

    content()
}