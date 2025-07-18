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
    rating = String.format("%.1f", rating / 2).toFloat(),
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
    rating = String.format("%.1f", rating.toFloat() / 2).toFloat(),
    date = date.toString(),
    description = description
)