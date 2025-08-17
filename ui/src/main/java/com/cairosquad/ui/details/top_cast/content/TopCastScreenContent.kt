package com.cairosquad.ui.details.top_cast.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.details.composable.BlurredCircle
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState

@Composable
fun TopCastScreenContent(
    onBackClick: () -> Unit,
    onClick: (Long) -> Unit,
    state: TopCastScreenState
) {
    Box {
        BlurredCircle()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            AppBar(
                modifier = Modifier.padding(bottom = 16.dp),
                title = stringResource(R.string.top_cast),
                onBackButtonClicked = onBackClick,
            )
            TopCastSection(state, onClick)
        }
    }
}