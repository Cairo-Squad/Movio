package com.cairosquad.ui.details.episodes.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme

@Composable
fun EpisodeCard(
    modifier: Modifier = Modifier,
    episodeName: String,
    episodeNumber: String,
    episodeDuration: Int,
    episodeRating: String,
    episodeImageUrl: String? = null
) {
    Row(
        modifier = modifier
            .padding(bottom = 12.dp)
            .padding(horizontal = 16.dp)
            .background(Theme.color.surfaces.surface)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EpisodeImage(episodeImageUrl)
        Column(
            modifier = Modifier.padding(end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EpisodeRating(episodeName, episodeRating)
            EpisodeDuration(episodeNumber, episodeDuration)
        }
    }
}

