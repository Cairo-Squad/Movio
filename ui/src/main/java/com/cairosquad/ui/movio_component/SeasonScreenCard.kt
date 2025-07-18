package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer

@Composable
fun SeasonScreenCard(
    movieTitle: String,
    movieRate: Float,
    totalNumberOfEpisodes: String,
    movieImage: String?,
    yearOfPublish: String,
    timeOfPublish: String,
    currentSeason: String,
    height: Dp,
    width: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = Theme.color.surfaces.surface),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (movieImage?.isNotEmpty() == true) {
            SafeImageViewer(
                model = "https://image.tmdb.org/t/p/w500$movieImage",
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier
                    .size(width = width, height = height)
                    .clip(RoundedCornerShape(8.dp)),
                loadingPlaceholder = {
                    LoadingMovieImage(
                        Modifier.fillMaxSize()
                    )
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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

        Column(
            modifier = modifier
                .height(height)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.season, currentSeason),
                    style = Theme.textStyle.title.mediumMedium14,
                    color = Theme.color.surfaces.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(R.string.rating_star),
                    tint = Theme.color.system.warning,
                )
                Text(
                    text = movieRate.toString(),
                    color = Theme.color.system.onWarning,
                    style = Theme.textStyle.label.smallRegular12
                )
            }

            Column(
                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.year_episodes,
                        yearOfPublish,
                        totalNumberOfEpisodes
                    ),
                    style = Theme.textStyle.label.smallRegular12,
                    color = Theme.color.surfaces.onSurfaceContainer
                )
                Spacer(modifier = modifier.weight(1f))
                Text(
                    text = stringResource(
                        R.string.season_premiere,
                        currentSeason,
                        movieTitle,
                        timeOfPublish
                    ),
                    style = Theme.textStyle.label.smallRegular12,
                    color = Theme.color.surfaces.onSurfaceContainer
                )
            }
        }
    }
}

@MultiThemePreviews
@Composable
private fun SeasonCardPreview() {
    MovioTheme {
        SeasonScreenCard(
            movieTitle = "Harry Potter and the Prisoner of Azkaban",
            movieImage = "https://www.behance.net/gallery/209589895/-2013/modules/1190800775",
            movieRate = 3.5f,
            width = 76.dp,
            height = 100.dp,
            totalNumberOfEpisodes = "1",
            onClick = {},
            yearOfPublish = "2004",
            currentSeason = "7",
            timeOfPublish = "october 4 , 2002"
        )
    }
}
