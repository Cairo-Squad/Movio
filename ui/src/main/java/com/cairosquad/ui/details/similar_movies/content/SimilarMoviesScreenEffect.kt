package com.cairosquad.ui.details.similar_movies.content

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesEffect
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel

@Composable
fun SimilarMoviesScreenEffects(
    viewModel: SimilarMoviesViewModel,
    navController: NavController
) {
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            is SimilarMoviesEffect.NavigateBack -> navController.popBackStack()

            is SimilarMoviesEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }
        }
    }
}
