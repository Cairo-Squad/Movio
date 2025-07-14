package com.cairosquad.viewmodel.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

fun Movie.toUiState() = SearchScreenState.MovieUiState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)

fun Artist.toUiState() = SearchScreenState.ArtistUiState(
    id = id,
    name = name,
    photoPath = photoPath
)

fun Series.toUiState() = SearchScreenState.SeriesUiState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)