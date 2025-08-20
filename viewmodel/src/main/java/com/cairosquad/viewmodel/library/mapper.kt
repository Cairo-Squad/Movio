package com.cairosquad.viewmodel.library

import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.localizeNumbers

fun MediaList.toUiState() = LibraryScreenState.ListsUiState(
    id = id,
    name = name.localizeNumbers(),
    mediaCount = mediaCount
)

fun Series.toUiState() = LibraryScreenState.SeriesUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name.localizeNumbers() },
    seasonsCount = seasonsCount,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    overview = overview.localizeNumbers(),
    trailerPath = trailerPath
)

fun Movie.toUiState() = LibraryScreenState.MovieUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name.localizeNumbers() },
    overview = overview.localizeNumbers(),
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    runtimeMinutes =
        runtimeMinutes
            .takeIf { it != 0 }
            ?.let { TimeUtil.convertIntToHourMinuteFormat(it) },
    trailerPath = trailerPath
)