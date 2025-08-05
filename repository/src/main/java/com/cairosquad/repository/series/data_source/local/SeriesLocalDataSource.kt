package com.cairosquad.repository.series.data_source.local

import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

interface SeriesLocalDataSource {
    suspend fun insertCacheCodeWithSeries(cacheCodeWithSeries: CacheCodeWithSeriesCacheDto)

    suspend fun getSeriesByCacheCode(cacheCode: String): List<SeriesCacheDto>

    suspend fun insertSeriesGenres(genres: List<GenreOfSeriesCacheDto>)

    suspend fun getSeriesGenres(): List<GenreOfSeriesCacheDto>

    suspend fun getSeriesReviewsByCacheCode(cacheCode: String): List<ReviewCacheDto>

    suspend fun insertCacheCodeWithReviews(cacheCodeWithReviewsCacheDto: CacheCodeWithReviewsCacheDto)

    suspend fun deleteExpiredCache(timestamp: Long)
}