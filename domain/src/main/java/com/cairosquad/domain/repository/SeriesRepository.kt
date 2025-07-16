package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series

interface SeriesRepository {
    suspend fun getSeries(seriesId: Long): Series

    suspend fun getSeriesReviews(seriesId: Long): List<Review>

    suspend fun getSeriesSeasons(seriesId: Long): List<Season>

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode>

    suspend fun getSimilarSeries(seriesId: Long): List<Series>

    suspend fun getSeriesTopCast(seriesId: Long): List<Artist>
}