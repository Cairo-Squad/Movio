package com.cairosquad.viewmodel.library.view_all_history

import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil

fun Series.toUiState() = ViewAllHistoryScreenState.SeriesUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name },
    seasonsCount = seasonsCount,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    overview = overview,
    trailerPath = trailerPath
)

fun Movie.toUiState() = ViewAllHistoryScreenState.MovieUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name },
    overview = overview,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    runtimeMinutes = TimeUtil.convertIntToHourMinuteFormat(runtimeMinutes),
    trailerPath = trailerPath
)