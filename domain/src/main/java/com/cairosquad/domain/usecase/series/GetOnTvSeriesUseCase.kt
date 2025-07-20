package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetOnTvSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getOnTvSeries(page: Int): List<Series> {
        return seriesRepository.getOnTvSeries(page)
    }
}