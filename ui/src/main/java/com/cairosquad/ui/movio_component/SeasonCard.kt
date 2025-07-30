package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig

@Composable
fun SeasonCard(
    seriesName: String,
    seasonTitle: String,
    seasonRate: Float,
    totalNumberOfEpisodes: String,
    movieImage: String?,
    yearOfPublish: String,
    timeOfPublish: String,
    currentSeason: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        if (movieImage?.isNotEmpty() == true) {
            SafeImageViewer(
                model = BuildConfig.IMAGE_BASE_URL + movieImage,
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier
                    .size(width = 76.dp, height = 100.dp)
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
                    .size(width = 76.dp, height = 100.dp)
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
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(start = 8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicText(
                text = seasonTitle,
                style = Theme.textStyle.title.mediumMedium14.copy(
                    color = Theme.color.surfaces.onSurface,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            BasicText(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(
                    R.string.year_episodes,
                    yearOfPublish,
                    totalNumberOfEpisodes
                ),
                style = Theme.textStyle.body.smallRegular10.copy(
                    color = Theme.color.surfaces.onSurfaceContainer
                ),
            )
            BasicText(
                text = stringResource(
                    R.string.season_premiere,
                    currentSeason,
                    seriesName,
                    timeOfPublish
                ),
                style = Theme.textStyle.label.smallRegular12.copy(
                    color = Theme.color.surfaces.onSurfaceContainer
                ),
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(R.string.rating_star),
                    tint = Theme.color.system.warning,
                )
                BasicText(
                    text = seasonRate.toString(),
                    style = Theme.textStyle.label.smallRegular12.copy(
                        color = Theme.color.system.onWarning,
                    )
                )
            }
        }
    }
}

@MultiThemePreviews
@Composable
private fun SeasonCardPreview() {
    MovioTheme {
        SeasonCard(
            seriesName = "Harry Potter",
            seasonTitle = "Harry Potter and the Prisoner of Azkaban",
            movieImage = "https://www.behance.net/gallery/209589895/-2013/modules/1190800775",
            seasonRate = 3.5f,
            totalNumberOfEpisodes = "1",
            onClick = {},
            yearOfPublish = "2004",
            currentSeason = "7",
            timeOfPublish = "october 4 , 2002"
        )
    }
}
