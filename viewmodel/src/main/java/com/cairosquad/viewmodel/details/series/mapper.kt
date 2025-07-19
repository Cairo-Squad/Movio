package com.cairosquad.viewmodel.details.series

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil

fun Artist.toUiState() = SeriesDetailsScreenState.ArtistUiState(
    id = id,
    name = name,
    photoPath = photoPath
)

fun Series.toUiState() = SeriesDetailsScreenState.SeriesUiState(
    id = 0L,
    title = title,
    rating = String.format("%.1f", if (rating != 0f) rating / 2 else 0f).toFloat(),
    posterPath = posterPath,
    genres = genres.map { it.name },
    seasonsCount = seasonsCount,
    releaseDate = TimeUtil.convertLongToTime(releaseDate),
    overview = overview,
    trailerPath = trailerPath
)

fun Season.toUiState() = SeriesDetailsScreenState.SeasonUiState(
    number = seasonNumber,
    name = seasonName,
    episodesCount = episodesCount,
    rating = String.format("%.1f", if (rating != 0f) rating / 2 else 0f).toFloat(),
    posterPath = posterPath,
    overview = overview,
    airDate = TimeUtil.convertLongToYear(airDate)
)

fun Review.toUiState() = SeriesDetailsScreenState.ReviewUiState(
    id = id,
    author = author,
    authorPhotoPath = authorPhotoPath,
    rating = String.format("%.1f", if (rating.toFloat() != 0f) rating.toFloat() / 2 else 0f)
        .toFloat(),
    date = TimeUtil.convertLongToNamedDate(date),
    description = description
)