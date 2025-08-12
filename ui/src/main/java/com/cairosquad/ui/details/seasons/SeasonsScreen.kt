package com.cairosquad.ui.details.seasons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.details.seasons.content.SeasonScreenEffects
import com.cairosquad.ui.details.seasons.content.SeasonsScreenContent
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.details.series.season.SeasonsViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun SeasonsScreen(
    seriesId: Long,
) {
    val viewModel: SeasonsViewModel =
        hiltViewModel<SeasonsViewModel, SeasonsViewModel.Factory> { factory ->
            factory.create(
                seriesId = seriesId,
                dispatcher = Dispatchers.IO
            )
        }
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    SeasonScreenEffects(viewModel, navController)

    SeasonsScreenContent(
        uiState = uiState,
        listener = viewModel,
    )
}