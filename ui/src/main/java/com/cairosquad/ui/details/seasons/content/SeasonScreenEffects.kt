package com.cairosquad.ui.details.seasons.content

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cairosquad.ui.navigation.EpisodesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.series.season.SeasonDetailEffect
import com.cairosquad.viewmodel.details.series.season.SeasonsViewModel

@Composable
fun SeasonScreenEffects(
    viewModel: SeasonsViewModel,
    navController: NavHostController
) {
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
}