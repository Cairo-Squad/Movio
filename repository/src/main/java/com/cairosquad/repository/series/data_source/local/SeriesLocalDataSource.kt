package com.cairosquad.repository.series.data_source.local

import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.RequestWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.utils.sharedDto.local.RequestWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

interface SeriesLocalDataSource {
    suspend fun insertRequestWithSeries(requestWithSeries: RequestWithSeriesCacheDto)

    suspend fun getSeriesByRequest(request: String): List<SeriesCacheDto>

    suspend fun insertSeriesGenres(genres: List<GenreOfSeriesCacheDto>)

    suspend fun getSeriesGenres(): List<GenreOfSeriesCacheDto>

    suspend fun getSeriesReviewsByRequest(request: String): List<ReviewCacheDto>

    suspend fun insertRequestWithReviews(requestWithReviewsCacheDto: RequestWithReviewsCacheDto)

    suspend fun deleteExpiredCache(timestamp: Long)
}