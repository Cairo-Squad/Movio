package com.cairosquad.viewmodel.rated.mappers

import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.rated.RatedItemUiState

/**
 * Maps a Movie entity to a RatedItemUiState
 */
fun Movie.toRatedItemUiState(): RatedItemUiState = RatedItemUiState(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate.toString(),
    rating = rating.toDouble(),
    isMovie = true
)

/**
 * Maps a Series entity to a RatedItemUiState
 */
fun Series.toRatedItemUiState(): RatedItemUiState = RatedItemUiState(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate.toString(),
    rating = rating.toDouble(),
    isMovie = false
)
