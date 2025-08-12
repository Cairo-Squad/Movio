package com.cairosquad.viewmodel.details.artist

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Artist.toArtistUiState() = ArtistScreenState.ArtistUiState(
    id = id,
    name = name,
    photoPath = photoPath,
    country = country,
    birthDate = TimeUtil.convertLongToNamedDate(birthDate),
    biography = biography,
    department = department
)

fun Movie.toArtistMovieUiState() = ArtistScreenState.MovieUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)

fun Series.toArtistSeriesUiState() = ArtistScreenState.SeriesUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)

