package com.cairosquad.ui.details.movie.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cairosquad.viewmodel.details.movie.MovieScreenState

fun LazyListScope.MovieImageSection(state: MovieScreenState) {
    item {
        when (state.basicDetailsSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingMovieImage(
                        modifier = Modifier.size(height = 260.dp, width = 200.dp)
                    )
                }
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.movie.posterPath.isNotEmpty()) {
                        SafeImageViewer(
                            modifier = Modifier
                                .size(height = 260.dp, width = 200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = BuildConfig.IMAGE_BASE_URL + state.movie.posterPath,
                            contentDescription = "",
                            loadingPlaceholder = {
                                LoadingMovieImage(
                                    modifier = Modifier.size(
                                        height = 260.dp,
                                        width = 200.dp
                                    )
                                )
                            }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(height = 260.dp, width = 200.dp)
                                .clip(RoundedCornerShape(8.dp))
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
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
    }
}