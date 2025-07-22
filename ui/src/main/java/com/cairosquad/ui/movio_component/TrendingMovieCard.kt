package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Chip
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.R
@Composable
fun TrendingMovieCard(
    imgUrl: String,
    movieTitle: String,
    movieCategory: String,
    rating: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        if (imgUrl?.isNotEmpty() == true) {
            SafeImageViewer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(76.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = "https://image.tmdb.org/t/p/w500$imgUrl",
                contentDescription = stringResource(com.cairosquad.design_system.R.string.movie_poster),
                loadingPlaceholder = { LoadingMovieImage(Modifier.fillMaxSize()) }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(76.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Theme.color.system.defaultImageBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(id = com.cairosquad.design_system.R.drawable.image_icon),
                    contentDescription = stringResource(com.cairosquad.design_system.R.string.default_image_icon),
                    tint = Color(0xFFEFF1F5)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Column {
                Text(
                    movieTitle,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.title.mediumMedium14,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.cairosquad.design_system.R.drawable.star),
                        contentDescription = stringResource(R.string.rating_star),
                        tint = Color.Unspecified
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = rating,
                        color = Theme.color.system.onWarning,
                        style = Theme.textStyle.label.smallRegular12
                    )

                }

            }
            Chip(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(24.dp),
                title = movieCategory,
                textStyle = Theme.textStyle.label.smallRegular12,
                isEnable = false
            )
        }
    }
}

@Preview
@Composable
private fun TrendingMovieCardPrevNight() {
    MovioTheme(isDarkTheme = true) {
        TrendingMovieCard("", movieTitle = "Ocean with David Attenborough", movieCategory = "Documentary", rating = "4.5")
    }
}

@Preview
@Composable
private fun TrendingMovieCardPrevLight() {
    MovioTheme(isDarkTheme = false) {
        TrendingMovieCard("", movieTitle = "Ocean with David Attenborough", movieCategory = "Documentary", rating = "4.5")
    }
}