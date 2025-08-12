package com.cairosquad.ui.details.series.content

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.EpisodesRoute
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.SeasonsRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.navigation.SimilarSeriesRoute
import com.cairosquad.ui.navigation.TopCastRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.ShareUtil
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.series.SeriesDetailEffect
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel

@Composable
fun SeriesScreenEffects(
    viewModel: SeriesDetailsViewModel,
    navController: NavHostController,
    uiState: SeriesDetailsScreenState,
    context: Context,
    seriesId: Long
) {
    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {

            SeriesDetailEffect.NavigateBack -> {
                navController.popBackStack()
            }

            SeriesDetailEffect.PlayTrailer -> {
                if (uiState.series.trailerPath.isBlank()) {
                    Toast.makeText(
                        context,
                        context.getString(com.cairosquad.ui.R.string.no_trailer_found_for_this_series),
                        Toast.LENGTH_LONG
                    ).show() // TODO: Change to snack bar
                } else {
                    ShareUtil.playOnYoutube(videoId = uiState.series.trailerPath, context = context)
                }
            }

            is SeriesDetailEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
                // TODO("Replace it with SnackBar")
            }

            is SeriesDetailEffect.NavigateToAllArtists -> {
                navController.navigate(TopCastRoute(seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllSeasons -> {
                navController.navigate(SeasonsRoute(seriesId))
            }

            is SeriesDetailEffect.NavigateToAllSimilar -> {
                navController.navigate(SimilarSeriesRoute(seriesId))
            }

            is SeriesDetailEffect.NavigateToArtistDetails -> {
                navController.navigate(ArtistRoute(effect.artistId))
            }

            is SeriesDetailEffect.NavigateToSeasonDetails -> {
                navController.navigate(
                    EpisodesRoute(
                        seriesId = effect.seriesId,
                        seasonNumber = effect.seasonNumber
                    )
                )
            }

            is SeriesDetailEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }

            SeriesDetailEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute)
            }
        }
    }
}