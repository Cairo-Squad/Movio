package com.cairosquad.ui.details.episodes.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailEffect
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsViewModel

@Composable
fun EpisodesScreenEffects(
    viewModel: EpisodesDetailsViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            EpisodesDetailEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is EpisodesDetailEffect.ShowSnackBar -> {
                viewModel.updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessage = context.getString(errorStatusToMessageResource(effect.message)),
                        isProcessSuccess = false
                    )
                }
            }

            EpisodesDetailEffect.PlayEpisode -> {}
        }
    }
}