package com.cairosquad.ui.details.artist.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistBiography(state: ArtistScreenState) {
    when (state.screenStatus) {
        ArtistScreenState.ScreenStatus.LOADING -> {
            LoadingMovieImage(
                Modifier
                    .padding(16.dp)
                    .height(120.dp)
                    .fillMaxWidth()
            )
        }

        ArtistScreenState.ScreenStatus.SUCCESS -> {
            if (state.artist.biography.isNotBlank()) {
                ExpandableText(
                    text = state.artist.biography,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.label.smallRegular14,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    collapsedMaxLine = 5,
                    showMoreText = stringResource(R.string.read_more_with_dotes_behind),
                    showMoreColor = Theme.color.surfaces.onSurfaceVariant,
                    showLessText = stringResource(R.string.read_less_with_dotes_behind)
                )
            }
        }

        ArtistScreenState.ScreenStatus.FAILED -> {}
    }
}