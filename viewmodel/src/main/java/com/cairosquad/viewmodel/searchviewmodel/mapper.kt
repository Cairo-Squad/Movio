package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

fun Movie.toUiState() = SearchScreenState.MovieScreenState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)

fun Artist.toUiState() = SearchScreenState.ArtistScreenState(
    id = id,
    name = name,
    photoPath = photoPath
)

fun Series.toUiState() = SearchScreenState.SeriesScreenState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)