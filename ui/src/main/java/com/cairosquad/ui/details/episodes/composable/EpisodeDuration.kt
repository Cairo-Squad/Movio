package com.cairosquad.ui.details.episodes.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme

@Composable
fun EpisodeDuration(episodeNumber: String, episodeDuration: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BasicText(
            text = stringResource(com.cairosquad.ui.R.string.episode, episodeNumber),
            style = Theme.textStyle.label.smallRegular12.copy(
                color = Theme.color.surfaces.onSurfaceContainer,
            )
        )
        BasicText(
            text = " • ",
            style = Theme.textStyle.label.smallRegular12.copy(
                color = Theme.color.surfaces.onSurfaceContainer,
            )
        )
        BasicText(
            text = stringResource(com.cairosquad.ui.R.string.m, episodeDuration),
            style = Theme.textStyle.label.smallRegular12.copy(
                color = Theme.color.surfaces.onSurfaceContainer,
            )
        )
    }
}
