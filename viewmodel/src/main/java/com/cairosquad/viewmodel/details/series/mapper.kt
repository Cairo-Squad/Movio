package com.cairosquad.viewmodel.details.series

import com.cairosquad.entity.Artist
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Artist.toUiState() = SeriesDetailsScreenState.ArtistUiState(
    id = id,
    name = name,
    photoPath = photoPath
)

fun Series.toUiState(): SeriesDetailsScreenState.SeriesUiState {
    return SeriesDetailsScreenState.SeriesUiState(
        id = id,
        title = title,
        rating = rating.roundToFirstDecimalPlace(),
        posterPath = posterPath,
        genres = genres.map { it.name },
        seasonsCount = seasonsCount,
        releaseDate = TimeUtil.convertLongToTime(releaseDate),
        overview = overview,
        trailerPath = trailerPath
    )
}

fun Season.toUiState() = SeriesDetailsScreenState.SeasonUiState(
    number = seasonNumber,
    name = seasonName,
    episodesCount = episodesCount,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    overview = overview,
    airDate = TimeUtil.convertLongToYear(airDate)
)

fun Review.toUiState() = SeriesDetailsScreenState.ReviewUiState(
    id = id,
    author = author,
    authorPhotoPath = authorPhotoPath,
    rating = rating.roundToFirstDecimalPlace(),
    date = TimeUtil.convertLongToNamedDate(date),
    description = description
)

fun MediaList.toUiState() = SeriesDetailsScreenState.SeriesList(
    id = id,
    name = name
)