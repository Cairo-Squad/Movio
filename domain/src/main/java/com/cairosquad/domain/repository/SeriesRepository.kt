package com.cairosquad.domain.repository

import com.cairosquad.domain.model.SortType
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series

interface SeriesRepository {
    suspend fun getSeriesById(seriesId: Long): Series

    suspend fun getSeriesReviews(seriesId: Long, page: Int): List<Review>

    suspend fun getSeriesSeasons(seriesId: Long): List<Season>

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode>

    suspend fun getSimilarSeries(seriesId: Long, page: Int): List<Series>

    suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<Artist>

    suspend fun getTopRatingSeries(page: Int, genreId: Long?): List<Series>

    suspend fun getMoreRecommendedSeries(page: Int, genreId: Long?): List<Series>

    suspend fun getOnTvSeries(page: Int, genreId: Long?): List<Series>

    suspend fun getAiringTodaySeries(page: Int, genreId: Long?): List<Series>

    suspend fun getTrendingSeries(page: Int, genreId: Long?): List<Series>

    suspend fun getFreeToWatchSeries(page: Int, genreId: Long?): List<Series>

    suspend fun getSeriesByCategory(genreId: Long, page: Int): List<Series>

    suspend fun getSeriesGenres(): List<Genre>

    suspend fun getPopularSeries(page: Int, genreId: Long?): List<Series>

    suspend fun getAllSeries(page: Int, genreId: Long?, sortType: SortType?): List<Series>

    suspend fun getSeriesByQuery(query: String, page: Int): List<Series>
}