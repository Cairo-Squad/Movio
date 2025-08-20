package com.cairosquad.ui.library.content

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.library.composable.UndoSnackBar
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryEffect
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryViewModel

@Composable
fun ViewAllHistory(
    viewModel: ViewAllHistoryViewModel = hiltViewModel()
) {

    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ViewAllHistoryEffect.OnMovieClicked -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            ViewAllHistoryEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ViewAllHistoryEffect.OnSeriesClicked -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }
        }
    }
    Box {
        ViewAllHistoryContent(
            screenState = uiState,
            listener = viewModel
        )
        UndoSnackBar(
            messageId = uiState.snackBarMessageId,
            isVisible = uiState.showSnackBar,
            onUndoClicked = viewModel::onUndoClick,
        )
    }
}