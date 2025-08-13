package com.cairosquad.ui.details.artist.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.artist.ArtistEffect
import com.cairosquad.viewmodel.details.artist.ArtistViewModel

@Composable
fun ArtistScreenEffects(
    viewModel: ArtistViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is ArtistEffect.ErrorHappened -> {
                viewModel.updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessage = context.getString(errorStatusToMessageResource(effect.message)),
                        isProcessSuccess = false
                    )
                }
            }

            is ArtistEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is ArtistEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is ArtistEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }
        }
    }
}