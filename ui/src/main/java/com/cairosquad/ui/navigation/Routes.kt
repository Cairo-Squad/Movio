package com.cairosquad.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object AppRoute

@Serializable
data class MovieRoute(
    val movieId: Long
)

@Serializable
data class SeriesRoute(
    val seriesId: Long
)

@Serializable
data class ArtistRoute(
    val artistId: Long
)

@Serializable
data class SimilarMovieRoute(
    val movieId: Long
)

@Serializable
data class SimilarSeriesRoute(
    val seriesId: Long
)

@Serializable
data class TopCastRoute(
    val mediaId: Long,
    val isMovie: Boolean
)

@Serializable
data class ReviewsRoute(
    val mediaId: Long,
    val isMovie: Boolean
)

@Serializable
data class SeasonsRoute(
    val seriesId: Long
)

@Serializable
data class EpisodeRoute(
    val episodeId: Long
    )

@Serializable
data object ForYouRoute

