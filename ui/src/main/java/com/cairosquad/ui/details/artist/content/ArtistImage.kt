package com.cairosquad.ui.details.artist.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistImage(state: ArtistScreenState) {
    when (state.screenStatus) {
        ArtistScreenState.ScreenStatus.LOADING -> {
            LoadingMovieImage(
                Modifier
                    .padding(top = 31.dp)
                    .clip(CircleShape)
                    .size(160.dp)
            )
        }

        ArtistScreenState.ScreenStatus.SUCCESS -> {
            if (state.artist.photoPath.isNotEmpty()) {
                SafeImageViewer(
                    model = BuildConfig.IMAGE_BASE_URL + state.artist.photoPath,
                    modifier = Modifier
                        .padding(horizontal = 6.67.dp)
                        .padding(top = 31.dp)
                        .size(160.dp)
                        .clip(CircleShape),
                    contentDescription = stringResource(R.string.artist_image),
                    nudeThreshold = 0.0,
                    nonNudeThreshold = 0.0,
                    loadingPlaceholder = {
                        LoadingMovieImage(
                            Modifier
                                .clip(CircleShape)
                                .size(160.dp)
                        )
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.67.dp)
                        .padding(top = 31.dp)
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Theme.color.system.defaultImageBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.image_icon),
                        contentDescription = stringResource(R.string.default_image_icon),
                        tint = Color(0xFFEFF1F5)
                    )
                }
            }
        }

        ArtistScreenState.ScreenStatus.FAILED -> {}
    }
}
