package com.cairosquad.domain.search.usecase

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.entity.Series

class GetMediaUseCase {
    suspend fun getMovieById(id: Long): Movie { TODO() }

    suspend fun getSeriesById(id: Long): Series { TODO() }

    suspend fun getArtistById(id: Long): Artist { TODO() }

    suspend fun getGetTopByMediaId(id: Long): List<Artist> { TODO() }

    suspend fun getReviewsByMediaId(id: Long): List<Review> { TODO() }

    suspend fun getSeriesSeasonsBySeriesId(id: Long): List<Review> { TODO() }

    suspend fun getEpisodesBySeriesIdAndSeasonNumber(seriesId: Long, seasonNumber: Int): List<Episode> { TODO() }

    suspend fun getSimilarMovies(): List<Movie> { TODO() }

    suspend fun getSimilarSeries(): List<Series> { TODO() }

}