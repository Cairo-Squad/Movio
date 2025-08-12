package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.lazy.LazyListScope
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.BasicDetails
import com.cairosquad.ui.details.composable.BasicDetailsLoading
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SeriesBasicDetailsSection(
    uiState: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (uiState.basicDetailsSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                BasicDetailsLoading()
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                BasicDetails(
                    title = uiState.series.title,
                    genres = uiState.series.genres,
                    rating = uiState.series.rating,
                    releaseDate = uiState.series.releaseDate,
                    seasonsCount = uiState.series.seasonsCount,
                    onRateClicked = listener::onRateClicked,
                    onPlayTrailerClicked = listener::onPlayTrailerClicked,
                    onAddToListClicked = listener::onAddToListClicked,
                    isRated = uiState.isRated
                )
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}
