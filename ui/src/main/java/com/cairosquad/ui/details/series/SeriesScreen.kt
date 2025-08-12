package com.cairosquad.ui.details.series

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.details.series.content.SeriesScreenBottomSheets
import com.cairosquad.ui.details.series.content.SeriesScreenContent
import com.cairosquad.ui.details.series.content.SeriesScreenEffects
import com.cairosquad.ui.details.series.content.SeriesScreenSnackBar
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel

@Composable
fun SeriesScreen(
    seriesId: Long,
) {
    val viewModel: SeriesDetailsViewModel =
        hiltViewModel<SeriesDetailsViewModel, SeriesDetailsViewModel.Factory> { factory ->
            factory.create(seriesId)
        }
    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()


    SeriesScreenEffects(viewModel, navController, uiState, context, seriesId)
    Box {
        SeriesScreenContent(
            uiState = uiState,
            listener = viewModel
        )
        SeriesScreenBottomSheets(uiState, viewModel, seriesId)
        SeriesScreenSnackBar(uiState)
    }
}
