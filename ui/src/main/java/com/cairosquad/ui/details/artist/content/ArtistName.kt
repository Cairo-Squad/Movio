package com.cairosquad.ui.details.artist.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistName(state: ArtistScreenState) {
    when (state.screenStatus) {
        ArtistScreenState.ScreenStatus.LOADING -> {
            LoadingMovieImage(
                Modifier
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape)
                    .size(width = 60.dp, height = 32.dp)
            )
        }

        ArtistScreenState.ScreenStatus.SUCCESS -> {
            BasicText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = state.artist.name,
                style = Theme.textStyle.headline.mediumMedium18
                    .copy(color = Theme.color.surfaces.onSurface),
            )
        }

        ArtistScreenState.ScreenStatus.FAILED -> {}
    }
}