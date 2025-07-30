package com.cairosquad.viewmodel.details.similar_movies

import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesScreenState.SimilarMovieUiState
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Movie.toUiState(): SimilarMovieUiState {
    return SimilarMovieUiState(
        id = id,
        title = title,
        posterUrl = posterPath,
        rating = rating.roundToFirstDecimalPlace()
    )
}