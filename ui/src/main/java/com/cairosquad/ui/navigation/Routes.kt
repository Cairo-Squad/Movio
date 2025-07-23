package com.cairosquad.ui.navigation

import com.cairosquad.viewmodel.home.model.DiscoverType
import com.cairosquad.viewmodel.home.model.MediaType
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
data class EpisodesRoute(
    val seriesId: Long,
    val seasonNumber: Int
)

@Serializable
data object ForYouRoute

@Serializable
data class DiscoverRoute(
    val type: DiscoverType,
    val mediaType: MediaType
)






