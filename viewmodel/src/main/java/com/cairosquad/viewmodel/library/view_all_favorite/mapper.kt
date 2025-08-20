package com.cairosquad.viewmodel.library.view_all_favorite

import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Series.toUiState() = ViewAllFavoriteScreenState.SeriesUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map { it.name.localizeNumbers() },
    seasonsCount = seasonsCount,
    releaseDate = TimeUtil.convertLongToNamedDate(releaseDate),
    overview = overview.localizeNumbers(),
    trailerPath = trailerPath
)

fun Movie.toUiState() = ViewAllFavoriteScreenState.MovieUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
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