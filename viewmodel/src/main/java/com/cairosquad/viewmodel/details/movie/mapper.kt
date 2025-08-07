package com.cairosquad.viewmodel.details.movie

import com.cairosquad.entity.Artist
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Artist.toArtistUiState() = MovieScreenState.TopCastUiState(
    id = id,
    name = name,
    photoPath = photoPath,
)

fun Movie.toMovieUiState() = MovieScreenState.MovieDetailsUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map { it.name },
    overview = overview,
    releaseDate = TimeUtil.convertLongToTime(releaseDate),
    runtimeMinutes = TimeUtil.convertIntToHourMinuteFormat(runtimeMinutes),
    trailerPath = trailerPath
)

fun Review.toReviewUiState() = MovieScreenState.ReviewUiState(
    id = id,
    author = author,
    authorPhotoPath = authorPhotoPath,
    rating = rating.roundToFirstDecimalPlace(),
    date = TimeUtil.convertLongToNamedDate(date),
    description = description
)

fun MediaList.toUiState() = MovieScreenState.MoviesList(
    id = id,
    name = name
)