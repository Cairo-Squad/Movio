package com.cairosquad.viewmodel.details.similar_movies

sealed class SimilarMoviesEffect {
    data object NavigateBack : SimilarMoviesEffect()
    data class NavigateToMovieDetails(val movieId: Long) : SimilarMoviesEffect()
}