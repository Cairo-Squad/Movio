package com.cairosquad.ui.details.episodes.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme

@Composable
fun EpisodeRating(episodeName: String, episodeRating: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BasicText(
            text = episodeName,
            style = Theme.textStyle.title.mediumMedium14.copy(color = Theme.color.surfaces.onSurface),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.review_star),
            contentDescription = "Rating",
            modifier = Modifier.size(16.dp)
        )
        BasicText(
            text = episodeRating,
            style = Theme.textStyle.label.smallRegular12.copy(color = Theme.color.warning.onWarning)
        )
    }
}