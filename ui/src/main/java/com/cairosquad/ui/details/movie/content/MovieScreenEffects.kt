package com.cairosquad.ui.details.movie.content

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.SimilarMovieRoute
import com.cairosquad.ui.navigation.TopCastRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.ShareUtil
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.movie.MovieEffect
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.movie.MovieViewModel

@Composable
fun MovieScreenEffects(
    viewModel: MovieViewModel,
    movieId: Long,
    state: MovieScreenState
) {
    val context = LocalContext.current

    val navController = LocalNavController.current

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is MovieEffect.NavigateToActor -> {
                navController.navigate(ArtistRoute(effect.actorId))
            }

            is MovieEffect.NavigateToMovie -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is MovieEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show() // TODO: Change to snack bar
            }

            MovieEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MovieEffect.NavigateToAllActors -> {
                navController.navigate(TopCastRoute(movieId, isMovie = true))
            }

            is MovieEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(movieId, isMovie = true))
            }

            is MovieEffect.NavigateToSimilarMovies -> {
                navController.navigate(SimilarMovieRoute(movieId))
            }

            MovieEffect.PlayTrailer -> {
                if (state.movie.trailerPath.isBlank()) {
                    Toast.makeText(
                        context,
                        context.getString(com.cairosquad.ui.R.string.no_trailer_found_for_this_movie),
                        Toast.LENGTH_LONG
                    ).show() // TODO: Change to snack bar
                } else {
                    ShareUtil.playOnYoutube(videoId = state.movie.trailerPath, context = context)
                }
            }

            MovieEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute)
            }
        }
    }
}