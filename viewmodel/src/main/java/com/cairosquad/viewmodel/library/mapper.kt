package com.cairosquad.viewmodel.library

import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil

fun MediaList.toUiState() = LibraryScreenState.ListsUiState(
    id = id,
    name = name,
    mediaCount = mediaCount
)

fun Series.toUiState() = LibraryScreenState.SeriesUiState(
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

fun Movie.toUiState() = LibraryScreenState.MovieUiState(
    id = 0,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name },
    overview = overview,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    runtimeMinutes = TimeUtil.convertIntToHourMinuteFormat(runtimeMinutes),
    trailerPath = trailerPath
)