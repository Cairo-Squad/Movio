package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series

interface SeriesDetailsRepository {
    suspend fun getSeriesById(seriesId: Long): Series

    suspend fun getReviewsBySeriesId(seriesId: Long): List<Review>

    suspend fun getSeriesSeasonsBySeriesId(seriesId: Long): List<Season>

    suspend fun getEpisodesBySeriesIdAndSeasonNumber(seriesId: Long, seasonNumber: Int): List<Episode>

    suspend fun getSimilarSeries(seriesId: Long): List<Series>

    suspend fun getTopCastBySeriesId(seriesId: Long): List<Artist>
}