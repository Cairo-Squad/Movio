package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.details.composable.SimilarSeriesSection
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SimilarSeriesSection(
    state: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (state.similarSeriesSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.similar_series),
                    sectionLoadingItem = {
                        LoadingMovieCard(
                            height = 160.dp
                        )
                    }
                )
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                if (state.similarSeries.isNotEmpty()) {
                    SimilarSeriesSection(
                        similarSeries = state.similarSeries,
                        onSeriesClicked = listener::onSeriesClicked,
                        onActionClicked = { listener.onSeeAllSimilarClicked(seriesId = state.series.id) },
                    )
                }
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}