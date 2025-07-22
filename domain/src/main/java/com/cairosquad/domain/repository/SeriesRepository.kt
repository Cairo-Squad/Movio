package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series

interface SeriesRepository {
    suspend fun getSeries(seriesId: Long): Series

    suspend fun getSeriesReviews(seriesId: Long, page: Int): List<Review>

    suspend fun getSeriesSeasons(seriesId: Long): List<Season>

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode>

    suspend fun getSimilarSeries(seriesId: Long, page: Int): List<Series>

    suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<Artist>

    suspend fun getTopRatingSeries(page: Int): List<Series>

    suspend fun getMoreRecommendedSeries(page: Int): List<Series>

    suspend fun getOnTvSeries(page: Int): List<Series>

    suspend fun getAiringTodaySeries(page: Int): List<Series>

    suspend fun getTrendingSeries(page: Int): List<Series>

    suspend fun getFreeToWatchSeries(page: Int): List<Series>

    suspend fun getSeriesByCategory(category: String, page: Int): List<Series>

}