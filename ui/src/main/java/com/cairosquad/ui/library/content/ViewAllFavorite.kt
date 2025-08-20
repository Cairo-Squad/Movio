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
import com.cairosquad.viewmodel.library.view_all_favorite.ViewAllFavoriteEffect
import com.cairosquad.viewmodel.library.view_all_favorite.ViewAllFavoriteViewModel

@Composable
fun ViewAllFavorite(
    viewModel: ViewAllFavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            ViewAllFavoriteEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ViewAllFavoriteEffect.OnMovieClicked -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is ViewAllFavoriteEffect.OnSeriesClicked -> {
                navController.navigate(SeriesRoute(effect.seriesId))

            }
        }
    }
    Box {
        ViewAllFavoriteContent(
            uiState = uiState,
            listener = viewModel,
        )
        UndoSnackBar(
            messageId = uiState.snackMessageId,
            isVisible = uiState.showSnackBar,
            onUndoClicked = viewModel::onUndoClick
        )
    }
}