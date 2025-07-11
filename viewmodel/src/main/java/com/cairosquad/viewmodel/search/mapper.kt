package com.cairosquad.viewmodel.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

fun Movie.toUiState() = SearchUiState.MovieUiState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)

fun Artist.toUiState() = SearchUiState.ArtistUiState(
    id = id,
    name = name,
    photoPath = photoPath
)

fun Series.toUiState() = SearchUiState.SeriesUiState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)