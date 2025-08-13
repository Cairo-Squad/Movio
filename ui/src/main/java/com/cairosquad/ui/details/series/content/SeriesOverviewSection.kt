package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

fun LazyListScope.SeriesOverviewSection(
    state: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    item {
        when (state.basicDetailsSectionState) {
            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                LoadingMovieImage(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(height = 200.dp)
                        .padding(bottom = 32.dp)
                )
            }

            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                if (state.series.overview.isNotEmpty()) {
                    ExpandableText(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                        text = state.series.overview,
                        showMoreText = stringResource(R.string.read_more_with_dots_behind),
                        showLessText = stringResource(R.string.read_less_with_dots_behind),
                        color = Theme.color.surfaces.onSurface,
                        style = Theme.textStyle.label.smallRegular14,
                        showMoreStyle = Theme.textStyle.label.smallRegular14,
                        showMoreColor = Theme.color.surfaces.onSurfaceVariant,
                        showLessColor = Theme.color.surfaces.onSurfaceVariant,
                    )
                }
            }

            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                DetailsFailContent(onTryAgainClick = listener::onRefresh)
            }
        }
    }
}