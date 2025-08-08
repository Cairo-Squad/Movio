package com.cairosquad.viewmodel.library.view_all_favorite

import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Series.toUiState() = ViewAllFavoriteScreenState.SeriesUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map { it.name },
    seasonsCount = seasonsCount,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    overview = overview,
    trailerPath = trailerPath
)

fun Movie.toUiState() = ViewAllFavoriteScreenState.MovieUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map { it.name },
    overview = overview,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    runtimeMinutes = TimeUtil.convertIntToHourMinuteFormat(runtimeMinutes),
    trailerPath = trailerPath
)