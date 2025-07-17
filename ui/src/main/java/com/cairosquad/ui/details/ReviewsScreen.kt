package com.cairosquad.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Chip
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.navigation.LocalNavController

@Composable
fun ReviewsScreen(
    mediaId: Long,
    isMovie: Boolean, // false for series, true for movies,
) {
    val navController = LocalNavController.current

    val media = if (isMovie) "movie" else "series"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        BasicText(
            text = "these are reviews for $media with id: $mediaId",
            style = Theme.textStyle.title.largeBold16
                .copy(color = Theme.color.surfaces.onSurface),
        )
        Chip(
            title = "back",
            onClick = { navController.popBackStack() }
        )
    }
}