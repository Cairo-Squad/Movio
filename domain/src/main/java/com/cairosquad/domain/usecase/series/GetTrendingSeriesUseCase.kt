package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetTrendingSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getTrendingSeries(page: Int,categoryId : String?= null): List<Series> {
        return seriesRepository.getTrendingSeries(page,categoryId)
    }
}