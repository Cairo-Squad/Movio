package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.lazy.LazyListScope
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.BasicDetails
import com.cairosquad.ui.details.composable.BasicDetailsLoading
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SeriesBasicDetailsSection(
    state: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (state.basicDetailsSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                BasicDetailsLoading()
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                BasicDetails(
                    title = state.series.title,
                    genres = state.series.genres,
                    rating = state.series.rating,
                    releaseDate = state.series.releaseDate,
                    seasonsCount = state.series.seasonsCount,
                    onRateClicked = listener::onRateClick,
                    onPlayTrailerClicked = listener::onPlayTrailerClick,
                    onAddToListClicked = listener::onAddToListClick,
                    isRated = state.isRated
                )
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}
