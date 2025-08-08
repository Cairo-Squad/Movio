package com.cairosquad.ui.navigation

import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

@Serializable
data object OnboardingRoute

@Serializable
data object LoginRoute

@Serializable
data class ForgetPasswordWebViewRoute(
    val url: String
)

@Serializable
data class SignUpWebViewRoute(
    val url: String
)

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
data class SeeAllScreenRoute(
    val contentType: MediaContentType,
    val mediaType: MediaType
)

@Serializable
data object ViewAllListsRoute


@Serializable
data object ViewAllFavoritesRoute


@Serializable
data object ViewAllHistoryRoute

@Serializable
data class ListRoute(
    val listId: Long
)

@Serializable
data object MyRatingsRoute