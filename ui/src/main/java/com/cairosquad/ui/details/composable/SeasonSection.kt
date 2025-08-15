package com.cairosquad.ui.details.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.movio_component.SeasonCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

@Composable
fun SeasonSection(
    seriesName: String,
    seriesId: Long,
    seasons: List<SeriesDetailsScreenState.SeasonUiState>,
    onActionClicked: () -> Unit,
    onSeasonClicked: (Long, Int) -> Unit,
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.current_seasons),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        items(seasons) { season ->
            SeasonCard(
                modifier = Modifier.width(260.dp),
                seriesName = seriesName,
                seasonTitle = season.name,
                seasonRate = season.rating,
                totalNumberOfEpisodes = season.episodesCount.toString(),
                movieImage = BuildConfig.IMAGE_BASE_URL + season.posterPath,
                yearOfPublish = season.airDate,
                timeOfPublish = season.airDate,
                currentSeason = season.number.toString(),
                onClick = {
                    onSeasonClicked(seriesId, season.number)
                },
                bottomPadding = 10.dp,
            )
        }
    }
}