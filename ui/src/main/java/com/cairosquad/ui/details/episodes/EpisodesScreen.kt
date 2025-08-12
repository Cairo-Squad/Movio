package com.cairosquad.ui.details.episodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.details.episodes.content.EpisodesScreenContent
import com.cairosquad.ui.details.episodes.content.EpisodesScreenEffects
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsViewModel

@Composable
fun EpisodesScreen(
    seriesId: Long,
    seasonNumber: Int,
) {
    val viewModel: EpisodesDetailsViewModel =
        hiltViewModel<EpisodesDetailsViewModel, EpisodesDetailsViewModel.Factory> { factory ->
            factory.create(seriesId, seasonNumber)
        }
    val navController = LocalNavController.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    EpisodesScreenEffects(viewModel, navController)
    EpisodesScreenContent(uiState = uiState, listener = viewModel, seriesId)
}