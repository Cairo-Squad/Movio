package com.cairosquad.viewmodel.see_all

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series


fun Movie.toSeeAllMediaUiState() = SeeAllScreenState.MediaUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map(Genre::toSeeAllGenreUiState),
    isMovie = true
)

fun Series.toSeeAllMediaUiState() = SeeAllScreenState.MediaUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map(Genre::toSeeAllGenreUiState),
    isMovie = false
)

fun Genre.toSeeAllGenreUiState() = SeeAllScreenState.GenreUiState(
    id = id,
    name = name
)