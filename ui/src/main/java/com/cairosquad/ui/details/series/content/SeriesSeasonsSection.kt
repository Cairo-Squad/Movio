package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.SeasonSection
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SeriesSeasonsSection(
    state: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (state.seasonsSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.current_seasons),
                    sectionLoadingItem = {
                        LoadingMovieImage(
                            modifier = Modifier.size(
                                width = 260.dp,
                                height = 137.dp
                            )
                        )
                    }
                )
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                if (state.seasons.isNotEmpty()) {
                    SeasonSection(
                        seriesName = state.series.title,
                        seriesId = state.series.id,
                        seasons = state.seasons,
                        onActionClicked = { listener.onSeeAllSeasonsClick(seriesId = state.series.id) },
                        onSeasonClicked = listener::onSeasonClick
                    )
                }
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}