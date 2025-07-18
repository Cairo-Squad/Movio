package com.cairosquad.viewmodel.details.movie

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

fun Artist.toArtistUiState() = MovieScreenState.TopCastUiState(
    id = id,
    name = name,
    photoPath = photoPath,
)

fun Movie.toMovieUiState() = MovieScreenState.MovieDetailsUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { it.name },
    overview = overview,
    releaseDate = releaseDate.toString(),
    runtimeMinutes = runtimeMinutes
)

fun Review.toReviewUiState() = MovieScreenState.ReviewUiState(
    id = id,
    author = author,
    authorPhotoPath = authorPhotoPath,
    rating = rating.toFloat(),
    date = date.toString(),
    description = description
)