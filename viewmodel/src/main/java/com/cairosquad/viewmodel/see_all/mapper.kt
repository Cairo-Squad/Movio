package com.cairosquad.viewmodel.see_all

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace


fun Movie.toUiState() = SeeAllScreenState.MediaUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map(Genre::toUiState),
    isMovie = true
)

fun Series.toUiState() = SeeAllScreenState.MediaUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map(Genre::toUiState),
    isMovie = false
)

fun Genre.toUiState() = SeeAllScreenState.GenreUiState(
    id = id,
    name = name.localizeNumbers(),
)