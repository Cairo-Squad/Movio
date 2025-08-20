package com.cairosquad.ui.details.movie.content

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
                viewModel.updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessage = context.getString(errorStatusToMessageResource(effect.message)),
                        isProcessSuccess = false
                    )
                }
            }

            MovieEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MovieEffect.NavigateToAllActors -> {
                navController.navigate(TopCastRoute(effect.movieId, isMovie = true))
            }

            is MovieEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(effect.movieId, isMovie = true))
            }

            is MovieEffect.NavigateToSimilarMovies -> {
                navController.navigate(SimilarMovieRoute(effect.movieId))
            }

            MovieEffect.PlayTrailer -> {
                if (state.movie.trailerPath.isBlank()) {
                    viewModel.updateState {
                        it.copy(
                            showSnackBar = true,
                            snackMessageId = com.cairosquad.ui.R.string.no_trailer_found_for_this_movie,
                            isProcessSuccess = false
                        )
                    }
                } else {
                    ShareUtil.playOnYoutube(videoId = state.movie.trailerPath, context = context)
                }
            }

            MovieEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute)
                fun onLoginSuccess() {
                    navController.popBackStack()
                    viewModel.updateStateAfterLoggingIn()
                }
                navController
                    .getBackStackEntry(LoginRoute)
                    .savedStateHandle["onLoginSuccess"] = ::onLoginSuccess
            }
        }
    }
}