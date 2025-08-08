package com.cairosquad.ui.details.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.ActionBar
import java.util.Locale

@Composable
fun BasicDetails(
    title: String,
    genres: List<String>,
    rating: Float,
    releaseDate: String,
    seasonsCount: Int? = null,
    runtimeMinutes: String? = null,
    onRateClicked: () -> Unit,
    onPlayTrailerClicked: () -> Unit,
    onAddToListClicked: () -> Unit,
    isRated : Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        BasicText(
            modifier = Modifier.padding(bottom = 8.dp),
            text = title,
            style = Theme.textStyle.headline.mediumMedium18.copy(
                color = Theme.color.surfaces.onSurface
            )
        )
        BasicText(
            modifier = Modifier.padding(bottom = 12.dp),
            text = genres.joinToString(", "),
            style = Theme.textStyle.label.smallRegular14.copy(
                color = Theme.color.surfaces.onSurfaceVariant
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            InfoChip(
                text = String.format(Locale.getDefault(), "%.1f", rating),
                imgRes = R.drawable.review_star,
            )
            if (seasonsCount != null) {
                InfoChip(
                    text = stringResource(
                        R.string.seasons_count,
                        seasonsCount
                    ),
                    imgRes = R.drawable.ic_media
                )
            }
            if (runtimeMinutes != null) {
                InfoChip(
                    text = runtimeMinutes,
                    imgRes = R.drawable.time,
                )
            }
            InfoChip(
                text = releaseDate,
                imgRes = R.drawable.date,
            )
        }
        ActionBar(
            onRateClicked = onRateClicked,
            onPlayClicked = onPlayTrailerClicked,
            onAddToListClicked = onAddToListClicked,
            isRated = isRated
        )
    }
}