package com.cairosquad.ui.details.series.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

@Composable
fun BoxScope.SeriesScreenSnackBar(uiState: SeriesDetailsScreenState) {
    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp),
        visible = uiState.showSnackBar,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> 2 * fullHeight },
            animationSpec = tween(durationMillis = 600)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> 2 * fullHeight },
            animationSpec = tween(durationMillis = 600)
        )
    ) {
        SnackBar(
            imageVector = ImageVector.vectorResource(if (uiState.isProcessSuccess) R.drawable.archive_tick else R.drawable.danger),
            message = uiState.snackMessage.ifEmpty {
                stringResource(uiState.snackMessageId)
            },
            action = {}
        )
    }
}
