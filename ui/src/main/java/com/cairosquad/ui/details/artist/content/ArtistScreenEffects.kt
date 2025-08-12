package com.cairosquad.ui.details.artist.content

import android.widget.Toast
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
    artistViewModel: ArtistViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    ObserveAsEffect(artistViewModel.effect) { effect ->
        when (effect) {
            is ArtistEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
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