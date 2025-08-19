package com.cairosquad.viewmodel.home

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.home.HomeScreenState.MovieSectionsState
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace


fun Movie.toUiState() = HomeScreenState.MediaUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map(Genre::toUiState),
    isMovie = true
)

fun Series.toUiState() = HomeScreenState.MediaUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath,
    genres = genres.map(Genre::toUiState),
    isMovie = false
)

fun Genre.toUiState() = HomeScreenState.GenreUiState(
    id = id,
    name = name,
)
fun MovieSectionsState.getSectionUiStateByContentType(type: MediaContentType): List<HomeScreenState.MediaUiState> {
    return when (type) {
        MediaContentType.TOP_RATING -> topRating
        MediaContentType.NOW_PLAYING -> nowPlaying
        MediaContentType.UPCOMING -> upComing
        MediaContentType.MORE_RECOMMENDED -> moreRecommended
        else -> emptyList()
    }
}
fun HomeScreenState.SeriesSectionsState.getSectionUiStateByContentType(type: MediaContentType): List<HomeScreenState.MediaUiState> {
    return when (type) {
        MediaContentType.TOP_RATING -> topRating
        MediaContentType.AIRING_TODAY -> airingToday
        MediaContentType.ON_TV -> onTv
        MediaContentType.MORE_RECOMMENDED -> moreRecommended
        else -> emptyList()
    }
}