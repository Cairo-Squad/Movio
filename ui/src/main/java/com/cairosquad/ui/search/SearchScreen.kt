package com.cairosquad.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.ForYouRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.search.content.SearchScreenContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.search.SearchEffect
import com.cairosquad.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val navController = LocalNavController.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val state by viewModel.screenState.collectAsState()

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is SearchEffect.ErrorHappened -> {
                viewModel.updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessage = context.getString(errorStatusToMessageResource(effect.message)),
                        isProcessSuccess = false
                    )
                }
            }

            is SearchEffect.NavigateToArtistDetails -> {
                navController.navigate(ArtistRoute(effect.artistId))
            }

            is SearchEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            SearchEffect.NavigateToSeeAllForYouScreen -> {
                navController.navigate(ForYouRoute)
            }

            is SearchEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }

            is SearchEffect.HideKeyboard -> {
                keyboardController?.hide()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        SearchScreenContent(
            state = state,
            listener = viewModel,
            modifier = Modifier.matchParentSize()
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            visible = state.showSnackBar,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            SnackBar(
                imageVector = ImageVector.vectorResource(
                    if (state.isProcessSuccess) R.drawable.archive_tick else R.drawable.danger
                ),
                message = state.snackMessage,
                action = {
                    viewModel.updateState { it.copy(showSnackBar = false) }
                }
            )
        }
    }
}