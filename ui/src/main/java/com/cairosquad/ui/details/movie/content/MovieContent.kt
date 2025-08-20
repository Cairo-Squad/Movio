package com.cairosquad.ui.details.movie.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
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
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState

@Composable
fun MovieContent(
    state: MovieScreenState,
    listener: MovieInteractionListener,
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
        MovieScreenState.ScreenStatus.ERROR -> {
            DetailsFailContent(onTryAgainClick = { listener.onRefresh() })
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.color.surfaces.surface)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .verticalScroll(listState)
            ) {
                MovieBackgroundSection(state)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .then(
                            if (
                                state.showCreateListBottomSheet
                                || state.isAddToListBottomSheetOpen
                                || state.isRateBottomSheetOpen
                                || state.isShareBottomSheetOpen
                                || state.isNoAccountBottomSheetOpen
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
                    MovieImageSection(state)
                    movieBasicDetails(state, listener)
                    MovieDescriptionSection(state)
                    CastSection(state, listener)
                    ReviewsSection(state, listener)
                    SimilarMoviesSection(state, listener)
                }
            }
        }
    }
    AppBar(
        modifier = Modifier
            .background(animatedBrush)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        onBackButtonClicked = listener::onBackClick,
        onShareButtonClicked = listener::onShareClick,
        onFavoriteButtonClicked = listener::onFavoriteClick,
        isFavorite = state.isFavorite
    )
}