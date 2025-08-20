package com.cairosquad.viewmodel.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Movie.toUiState() = SearchScreenState.MovieUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)

fun Artist.toUiState() = SearchScreenState.ArtistUiState(
    id = id,
    name = name.localizeNumbers(),
    photoPath = photoPath
)

fun Series.toUiState() = SearchScreenState.SeriesUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)