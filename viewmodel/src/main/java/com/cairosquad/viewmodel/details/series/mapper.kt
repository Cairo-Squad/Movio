package com.cairosquad.viewmodel.details.series

import com.cairosquad.entity.Artist
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Artist.toUiState() = SeriesDetailsScreenState.ArtistUiState(
    id = id,
    name = name.localizeNumbers(),
    photoPath = photoPath
)

fun Series.toUiState(): SeriesDetailsScreenState.SeriesUiState {
    return SeriesDetailsScreenState.SeriesUiState(
        id = id,
        title = title.localizeNumbers(),
        rating = rating.roundToFirstDecimalPlace(),
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
}

fun Season.toUiState() = SeriesDetailsScreenState.SeasonUiState(
    number = seasonNumber,
    name = seasonName.localizeNumbers(),
    episodesCount = episodesCount,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    overview = overview.localizeNumbers(),
    airDate = TimeUtil.convertLongToYear(airDate)
)

fun Review.toUiState() = SeriesDetailsScreenState.ReviewUiState(
    id = id,
    author = author.localizeNumbers(),
    authorPhotoPath = authorPhotoPath,
    rating = rating.roundToFirstDecimalPlace(),
    date = TimeUtil.convertLongToNamedDate(date),
    description = description.localizeNumbers()
)

fun MediaList.toUiState() = SeriesDetailsScreenState.SeriesList(
    id = id,
    name = name.localizeNumbers()
)