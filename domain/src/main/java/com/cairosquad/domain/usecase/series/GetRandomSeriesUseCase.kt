package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetRandomSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getRandomSeries(page: Int): List<Series> {
        return seriesRepository.getRandomSeries(page)
    }
}