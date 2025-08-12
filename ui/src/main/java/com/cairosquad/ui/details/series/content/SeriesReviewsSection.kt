package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.details.composable.SeriesReviewSection
import com.cairosquad.ui.movio_component.LoadingReviewCard
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SeriesReviewsSection(
    uiState: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (uiState.reviewsSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.reviews),
                    sectionLoadingItem = {
                        LoadingReviewCard(
                            modifier = Modifier.size(
                                width = 260.dp,
                                height = 137.dp
                            )
                        )
                    }
                )
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                if (uiState.reviews.isNotEmpty()) {
                    SeriesReviewSection(
                        reviews = uiState.reviews,
                        onActionClicked = { listener.onSeeAllReviewsClicked(seriesId = uiState.series.id) }
                    )
                }
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}