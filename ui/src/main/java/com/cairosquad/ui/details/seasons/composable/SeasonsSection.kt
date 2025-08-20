package com.cairosquad.ui.details.seasons.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.SeasonCard
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState

@Composable
fun Seasons(
    state: SeasonDetailsScreenState,
    listener: SeasonDetailsInteractionListener
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        when (state.seasonSectionState) {
            SeasonDetailsScreenState.ScreenStatus.LOADING -> {
                items(10) {
                    LoadingMovieImage(Modifier
                        .height(100.dp)
                        .fillMaxWidth())
                }
            }

            SeasonDetailsScreenState.ScreenStatus.SUCCESS -> {
                items(state.season) { season ->
                    SeasonCard(
                        seriesName = state.seriesTitle,
                        seasonTitle = season.name,
                        seasonRate = season.rating,
                        totalNumberOfEpisodes = season.episodesCount.toString(),
                        movieImage = season.posterPath,
                        yearOfPublish = season.airDate,
                        timeOfPublish = season.timeOfPublish,
                        currentSeason = "${season.number}",
                        onClick = {
                            listener.onSeasonClicked(
                                season.seriesId,
                                season.number
                            )
                        },
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                    )
                }

            }

            SeasonDetailsScreenState.ScreenStatus.ERROR -> {
                item {
                    DetailsFailContent(onTryAgainClick = listener::onRefresh)
                }
            }
        }
    }
}