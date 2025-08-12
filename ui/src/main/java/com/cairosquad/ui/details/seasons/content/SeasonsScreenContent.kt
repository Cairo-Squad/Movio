package com.cairosquad.ui.details.seasons.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.details.composable.BlurredCircle
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState

@Composable
fun SeasonsScreenContent(
    uiState: SeasonDetailsScreenState,
    listener: SeasonDetailsInteractionListener,
) {
    Box {
        BlurredCircle()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            AppBar(
                title = stringResource(R.string.current_season),
                onBackButtonClicked = listener::onBackClicked,
            )
            Seasons(uiState, listener)
        }
    }
}