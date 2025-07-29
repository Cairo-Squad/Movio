package com.cairosquad.viewmodel.home

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series


fun Movie.toHomeMediaUiState() = HomeScreenState.MediaUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map(Genre::toHomeGenreUiState),
    isMovie = true
)

fun Series.toHomeMediaUiState() = HomeScreenState.MediaUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map(Genre::toHomeGenreUiState),
    isMovie = false
)

fun Genre.toHomeGenreUiState() = HomeScreenState.GenreUiState(
    id = id,
    name = name,
    nameResId = null
)