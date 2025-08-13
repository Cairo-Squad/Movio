package com.cairosquad.ui.details.series.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

@Composable
fun SeriesScreenContent(
    state: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    val listState = rememberScrollState()
    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 275.dp.toPx() }
    val progress = (listState.value / scrollThresholdPx).coerceIn(0f, 1f)

    val animatedStartColor = lerp(
        start = Theme.color.surfaces.statusBarShadow,
        stop = Theme.color.surfaces.surface,
        fraction = progress
    )
    val animatedEndColor = lerp(
        start = Color.Transparent,
        stop = Theme.color.surfaces.surface,
        fraction = progress
    )
    val animatedBrush = verticalGradient(
        colors = listOf(animatedStartColor, animatedEndColor)
    )

    when (state.basicDetailsSectionState) {
        SeriesDetailsScreenState.SectionStatus.ERROR -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                StateMessage(
                    imageDrawable =
                        if (Theme.isDark) R.drawable.no_internet_dark
                        else R.drawable.no_internet,
                    titleId = R.string.no_internet_connection,
                    descriptionId = R.string.internet_is_not_available_description
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.color.surfaces.surface)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .verticalScroll(listState)
            ) {
                SeriesBackgroundImage(state)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .then(
                            if (
                                state.showCreateListBottomSheet
                                || state.showRateBottomSheet
                                || state.showLoginBottomSheet
                                || state.showShareBottomSheet
                                || state.showAddToListBottomSheet
                            ) {
                                Modifier.blur(4.dp)
                            } else {
                                Modifier
                            }
                        )
                        .heightIn(max = 10000.dp),
                    horizontalAlignment = Alignment.Start,
                    userScrollEnabled = false
                ) {
                    SeriesImage(state, listener)
                    SeriesBasicDetailsSection(state, listener)
                    SeriesOverviewSection(state, listener)
                    SeriesTopCastSection(state, listener)
                    SeriesSeasonsSection(state, listener)
                    SeriesReviewsSection(state, listener)
                    SimilarSeriesSection(state, listener)
                }
            }
        }
    }
    AppBar(
        modifier = Modifier
            .background(brush = animatedBrush)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        onBackButtonClicked = listener::onBackClicked,
        onShareButtonClicked = listener::onShareClicked,
        onFavoriteButtonClicked = listener::onFavoriteClicked,
        isFavorite = state.isFavorite
    )
}