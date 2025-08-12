package com.cairosquad.ui.details.episodes.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer

@Composable
fun EpisodeImage(episodeImageUrl: String?) {
    Box {
        SafeImageViewer(
            contentDescription = "Episode Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 74.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = episodeImageUrl.toString()
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.4f))
                .blur(16.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(R.drawable.outline_play),
            contentDescription = "Play",
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 7.dp, height = 8.dp)
        )
    }
}