package com.cairosquad.ui.library.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.navigation.ListRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.library.view_all_lists.ViewAllListsEffect
import com.cairosquad.viewmodel.library.view_all_lists.ViewAllListsViewModel

@Composable
fun ViewAllLists(
    viewModel: ViewAllListsViewModel = hiltViewModel()
) {
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ViewAllListsEffect.OnMovieListClicked -> {
                navController.navigate(ListRoute(effect.listId, effect.listName))
            }

            ViewAllListsEffect.OnNavigateBack -> {
                navController.popBackStack()
            }

            is ViewAllListsEffect.OnSeriesListClicked -> {
                navController.navigate(ListRoute(effect.listId, effect.listName))
            }
        }
    }

    ViewAllListsContent(
        screenState = uiState,
        listener = viewModel
    )
}
