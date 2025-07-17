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
    rating = rating / 2,
    posterPath = posterPath,
    genres = genres.map { it.name },
    seasonsCount = seasonsCount,
    releaseDate = TimeUtil.convertLongToTime(releaseDate),
    overview = overview
)

fun Season.toUiState() = SeriesDetailsScreenState.SeasonUiState(
    number = seasonNumber,
    name = seasonName,
    episodesCount = episodesCount,
    rating = rating / 2,
    posterPath = posterPath,
    overview = overview,
    airDate = TimeUtil.convertLongToYear(airDate)
)

fun Review.toUiState() = SeriesDetailsScreenState.ReviewUiState(
    id = id,
    author = author,
    authorPhotoPath = authorPhotoPath,
    rating = rating.toFloat() / 2,
    date = TimeUtil.convertLongToNamedDate(date),
    description = description
)