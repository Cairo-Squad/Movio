package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetMoreRecommendedSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getMoreRecommendedSeries(page: Int): List<Series> {
        return seriesRepository.getMoreRecommendedSeries(page)
    }
}