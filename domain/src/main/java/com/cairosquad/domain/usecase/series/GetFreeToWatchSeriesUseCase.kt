package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetFreeToWatchSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getFreeToWatchSeries(page: Int): List<Series> {
        return seriesRepository.getFreeToWatchSeries(page)
    }
}