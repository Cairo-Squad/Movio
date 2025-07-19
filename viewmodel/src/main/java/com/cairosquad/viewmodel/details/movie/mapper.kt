package com.cairosquad.viewmodel.details.movie

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.util.TimeUtil

fun Artist.toArtistUiState() = MovieScreenState.TopCastUiState(
    id = id,
    name = name,
    photoPath = photoPath,
)

fun Movie.toMovieUiState() = MovieScreenState.MovieDetailsUiState(
    id = id,
    title = title,
    rating = (rating * 10).toInt().toFloat() / 10,
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
    rating = (rating * 10).toInt().toFloat() / 10.toFloat(),
    date = TimeUtil.convertLongToNamedDate(date),
    description = description
)