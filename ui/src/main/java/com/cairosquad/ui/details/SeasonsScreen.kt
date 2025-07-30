package com.cairosquad.ui.details

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.SeasonCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.EpisodesRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.series.season.SeasonDetailEffect
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState
import com.cairosquad.viewmodel.details.series.season.SeasonsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeasonsScreen(
    seriesId: Long,
    viewModel: SeasonsViewModel = koinViewModel { parametersOf(seriesId) }
) {
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            SeasonDetailEffect.NavigateBack -> navController.popBackStack()
            is SeasonDetailEffect.NavigateToEpisodesScreen -> navController.navigate(
                EpisodesRoute(
                    effect.seriesId,
                    effect.seasonNumber
                )
            )

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
) {
    Box {
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .size(230.dp)
                .align(Alignment.TopEnd)
                .then(
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        Modifier.dropShadow(
                            shape = CircleShape,
                            color = Theme.color.surfaces.onSurfaceAt5,
                            blur = 264.dp,
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            alpha = 0.10f
                        )
                    } else {
                        Modifier
                            .blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .background(
                                color = Theme.color.surfaces.onSurfaceAt5,
                                shape = CircleShape
                            )
                    }
                )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            stickyHeader {
                AppBar(
                    title = stringResource(R.string.current_season),
                    onBackButtonClicked = listener::onBackClicked
                )
            }
            when (uiState.seasonSectionState) {
                SeasonDetailsScreenState.ScreenStatus.LOADING -> {
                    items(10) {
                        LoadingMovieImage(
                            Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                SeasonDetailsScreenState.ScreenStatus.SUCCESS -> {
                    items(uiState.season) { season ->
                        SeasonCard(
                            seriesName = uiState.seriesTitle,
                            seasonTitle = season.name,
                            seasonRate = season.rating,
                            totalNumberOfEpisodes = season.episodesCount.toString(),
                            movieImage = season.posterPath,
                            yearOfPublish = season.airDate,
                            timeOfPublish = season.timeOfPublish,
                            currentSeason = "${season.number}",
                            onClick = { listener.onSeasonClicked(season.seriesId, season.number) },
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth(),
                        )
                    }

                }

                SeasonDetailsScreenState.ScreenStatus.ERROR -> {
                   item {
                       Box(
                           modifier = Modifier
                               .fillMaxSize(),
                           contentAlignment = Alignment.Center
                       ) {
                           StateMessage(
                               imageDrawable = R.drawable.no_internet,
                               titleId = R.string.no_internet_connection,
                               descriptionId = R.string.internet_is_not_available_description
                           )
                       }
                   }

                }
            }
        }
    }
}