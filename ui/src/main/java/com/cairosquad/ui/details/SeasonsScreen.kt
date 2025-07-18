package com.cairosquad.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.SeasonScreenCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.EpisodeRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.series.season.SeasonDetailEffect
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState
import com.cairosquad.viewmodel.details.series.season.SeasonViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeasonsScreen(
    seriesId: Long = 67197,
    seasonNumber: Int = 1,
    viewModel: SeasonViewModel = koinViewModel{ parametersOf(seriesId, seasonNumber) }
) {
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            SeasonDetailEffect.NavigateBack -> navController.popBackStack()
            is SeasonDetailEffect.NavigateToEpisodeDetails -> navController.navigate(EpisodeRoute(effect.episodeId, effect.seasonNumber))

        }
    }

    SeasonScreenContent(
        uiState = uiState,
        listener = viewModel
    )
}

@Composable
fun SeasonScreenContent(
    uiState: SeasonDetailsScreenState,
    listener: SeasonDetailsInteractionListener
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AppBar(
                    title = stringResource(R.string.current_season),
                    onBackButtonClicked = listener::onBackClicked,
                    modifier = Modifier.background(Theme.color.surfaces.surface)
                )
            }
            items(uiState.season) { season ->
                SeasonScreenCard(
                    movieTitle = season.name,
                    movieRate = season.rating,
                    totalNumberOfEpisodes = season.episodesCount.toString(),
                    movieImage = season.posterPath,
                    yearOfPublish = season.airDate,
                    timeOfPublish = season.airDate,
                    currentSeason = "${season.number}",
                    height = 100.dp,
                    width = 76.dp,
                    onClick = { listener.onEpisodeClicked(season.id, season.number) }
                )
            }

        }
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .size(230.dp)
                .blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(
                    color = Color(0x33734EF8),
                    shape = CircleShape
                )
                .align(Alignment.TopEnd)
        )
    }
}