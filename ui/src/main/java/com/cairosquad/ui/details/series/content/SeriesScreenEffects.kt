package com.cairosquad.ui.details.series.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.EpisodesRoute
import com.cairosquad.ui.navigation.LocalNavController
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
    state: SeriesDetailsScreenState,
) {
    val context = LocalContext.current

    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {

            SeriesDetailEffect.NavigateBack -> {
                navController.popBackStack()
            }

            SeriesDetailEffect.PlayTrailer -> {
                if (state.series.trailerPath.isBlank()) {
                    viewModel.showSnackBar(
                        messageId = com.cairosquad.ui.R.string.no_trailer_found_for_this_series,
                        isSuccessful = false
                    )
                } else {
                    ShareUtil.playOnYoutube(videoId = state.series.trailerPath, context = context)
                }
            }

            is SeriesDetailEffect.ErrorHappened -> {
                viewModel.showSnackBar(
                    messageId = errorStatusToMessageResource(effect.message),
                    isSuccessful = false
                )
            }

            is SeriesDetailEffect.NavigateToAllArtists -> {
                navController.navigate(TopCastRoute(effect.seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(effect.seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllSeasons -> {
                navController.navigate(SeasonsRoute(effect.seriesId))
            }

            is SeriesDetailEffect.NavigateToAllSimilar -> {
                navController.navigate(SimilarSeriesRoute(effect.seriesId))
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