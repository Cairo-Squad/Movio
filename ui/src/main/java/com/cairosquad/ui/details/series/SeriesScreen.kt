package com.cairosquad.ui.details.series

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.details.series.composable.SeriesScreenBottomSheets
import com.cairosquad.ui.details.series.content.SeriesScreenContent
import com.cairosquad.ui.details.series.content.SeriesScreenEffects
import com.cairosquad.ui.details.series.composable.SeriesScreenSnackBar
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel

@Composable
fun SeriesScreen(
    seriesId: Long,
) {
    val viewModel: SeriesDetailsViewModel =
        hiltViewModel<SeriesDetailsViewModel, SeriesDetailsViewModel.Factory> { factory ->
            factory.create(seriesId)
        }

    val state by viewModel.screenState.collectAsStateWithLifecycle()


    SeriesScreenEffects(viewModel, state)
    Box {
        SeriesScreenContent(
            state = state,
            listener = viewModel
        )
        SeriesScreenBottomSheets(state, viewModel)
        SeriesScreenSnackBar(state)
    }
}
