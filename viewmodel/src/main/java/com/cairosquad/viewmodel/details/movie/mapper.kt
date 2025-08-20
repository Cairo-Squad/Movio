package com.cairosquad.viewmodel.details.movie

import com.cairosquad.entity.Artist
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Artist.toUiState() = MovieScreenState.TopCastUiState(
    id = id,
    name = name.localizeNumbers(),
    photoPath = photoPath,
)

fun Movie.toUiState() = MovieScreenState.MovieDetailsUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map { it.name },
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

fun Review.toUiState() = MovieScreenState.ReviewUiState(
    id = id,
    author = author.localizeNumbers(),
    authorPhotoPath = authorPhotoPath,
    rating = rating.roundToFirstDecimalPlace(),
    date = TimeUtil.convertLongToNamedDate(date),
    description = description.localizeNumbers()
)

fun MediaList.toUiState() = MovieScreenState.MoviesList(
    id = id,
    name = name.localizeNumbers()
)