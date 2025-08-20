package com.cairosquad.ui.details.episodes.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsInteractionListener
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState
import com.cairosquad.viewmodel.util.toLocalString

@Composable
fun EpisodesScreenContent(
    state: EpisodesDetailsScreenState,
    listener: EpisodesDetailsInteractionListener,
    seriesId: Long,
    modifier: Modifier = Modifier,

    ) {
    val seasonOptions = state.seasons.map {
        stringResource(
            com.cairosquad.ui.R.string.season,
            it.seasonNumber.toLocalString()
        )
    }

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
    when (state.episodesSectionState) {
        EpisodesDetailsScreenState.ScreenStatus.ERROR -> {
            DetailsFailContent(onTryAgainClick = listener::onRefresh)

        }
        else->{
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Theme.color.surfaces.surface)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .verticalScroll(listState)
            ) {
                BackgroundImageSection(state)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .heightIn(max = 10000.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    userScrollEnabled = false,
                ) {
                    seasonImage(state)
                    seasonDetails(state, seasonOptions, listener, seriesId)
                    episodesSection(state)
                }
            }}
    }
    AppBar(
        modifier = Modifier
            .background(brush = animatedBrush)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        onBackButtonClicked = listener::onBackClick,
    )
}