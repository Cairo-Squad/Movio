package com.cairosquad.viewmodel.home

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series


fun Movie.toHomeMovieUiState() = HomeScreenState.MovieUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map(Genre::toHomeGenreUiState)
)

fun Series.toHomeSeriesUiState() = HomeScreenState.SeriesUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map(Genre::toHomeGenreUiState)
)

fun Genre.toHomeGenreUiState() = HomeScreenState.GenreUiState(id, name)