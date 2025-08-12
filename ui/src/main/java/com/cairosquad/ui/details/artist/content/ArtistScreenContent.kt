package com.cairosquad.ui.details.artist.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.viewmodel.details.artist.ArtistInteractionListener
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistScreenContent(
    modifier: Modifier = Modifier,
    state: ArtistScreenState,
    listener: ArtistInteractionListener
) {
    val listScroll = rememberScrollState()
    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 250.dp.toPx() }
    val progress = (listScroll.value / scrollThresholdPx).coerceIn(0f, 1f)

    val animatedStartColor =
        lerp(Theme.color.surfaces.statusBarShadow, Theme.color.surfaces.surface, progress)
    val animatedEndColor = lerp(Color.Transparent, Theme.color.surfaces.surface, progress)
    val animatedBrush = verticalGradient(colors = listOf(animatedStartColor, animatedEndColor))

    Box(modifier = modifier.fillMaxSize()) {
        if (state.screenStatus == ArtistScreenState.ScreenStatus.FAILED) {
            DetailsFailContent(onTryAgainClick = listener::onRefresh)
        }
        else{
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .verticalScroll(listScroll),
                horizontalAlignment = Alignment.Start,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    ArtistBackgroundImage(state = state)
                    ArtistImage(state)
                }
                ArtistName(state)
                ArtistDepartment(state)
                ArtistInformation(state)
                ArtistBiography(state)
                ArtistMoviesAndSeries(state, listener)
            }
        }
        AppBar(
            onBackButtonClicked = listener::onClickBack,
            modifier = Modifier
                .background(animatedBrush)
                .windowInsetsPadding(WindowInsets.statusBars)
        )
    }
}