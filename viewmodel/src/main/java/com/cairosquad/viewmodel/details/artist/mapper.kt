package com.cairosquad.viewmodel.details.artist

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Artist.toUiState() = ArtistScreenState.ArtistUiState(
    id = id,
    name = name.localizeNumbers(),
    photoPath = photoPath,
    country = country,
    birthDate = TimeUtil.convertLongToNamedDate(birthDate),
    biography = biography.localizeNumbers(),
    department = department.localizeNumbers()
)

fun Movie.toUiState() = ArtistScreenState.MovieUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)

fun Series.toUiState() = ArtistScreenState.SeriesUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)