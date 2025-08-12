package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.details.composable.SeriesTopCastSection
import com.cairosquad.ui.movio_component.LoadingArtistCard
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SeriesTopCastSection(
    uiState: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (uiState.castSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.top_cast),
                    sectionLoadingItem = {
                        LoadingArtistCard()
                    }
                )
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                if (uiState.cast.isNotEmpty()) {
                    SeriesTopCastSection(
                        onActionClicked = { listener.onSeeAllArtistsClicked(uiState.series.id) },
                        onArtistClicked = listener::onArtistClicked,
                        cast = uiState.cast
                    )
                }
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}