package com.cairosquad.viewmodel.library.list_content

import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.localizeNumbers

fun Series.toUiState() = ListContentScreenState.SeriesUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name },
    seasonsCount = seasonsCount,
    releaseDate =
        releaseDate
            .takeIf { it != 0L }
            ?.let { TimeUtil.convertLongToTime(it) },
    overview = overview.localizeNumbers(),
    trailerPath = trailerPath
)

fun Movie.toUiState() = ListContentScreenState.MovieUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name.localizeNumbers() },
    overview = overview.localizeNumbers(),
    releaseDate =
        releaseDate
            .takeIf { it != 0L }
            ?.let { TimeUtil.convertLongToTime(it) },
    runtimeMinutes =
        runtimeMinutes
            .takeIf { it != 0 }
            ?.let { TimeUtil.convertIntToHourMinuteFormat(it) },
    trailerPath = trailerPath
)