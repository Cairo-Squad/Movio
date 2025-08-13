package com.cairosquad.ui.details.episodes.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppDropDownMenu
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsInteractionListener
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState

fun LazyListScope.SeasonDetails(
    state: EpisodesDetailsScreenState,
    seasonOptions: List<String>,
    listener: EpisodesDetailsInteractionListener,
    seriesId: Long
) {
    item {
        when (state.basicDetailsSectionState) {
            EpisodesDetailsScreenState.ScreenStatus.LOADING -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LoadingMovieImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(height = 26.dp, width = 32.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    LoadingMovieImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(height = 26.dp, width = 96.dp)
                    )
                }
            }

            EpisodesDetailsScreenState.ScreenStatus.SUCCESS -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(
                            com.cairosquad.ui.R.string.episodes,
                            state.season.episodesCount
                        ),
                        style = Theme.textStyle.headline.mediumMedium18.copy(
                            color = Theme.color.surfaces.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AppDropDownMenu(
                        selectedText = stringResource(
                            R.string.season,
                            state.selectedSeasonNumber
                        ),
                        options = seasonOptions,
                        imgRes = R.drawable.drop_down_arrow,
                        onOptionSelected = { selectedSeason ->
                            val seasonNum =
                                seasonOptions.indexOfFirst {
                                    selectedSeason == it
                                }.takeIf { it != -1 } ?: 1
                            listener.onSeasonSelected(seriesId, seasonNum)
                        },
                    )
                }
            }

            EpisodesDetailsScreenState.ScreenStatus.ERROR -> {}
        }
    }
}