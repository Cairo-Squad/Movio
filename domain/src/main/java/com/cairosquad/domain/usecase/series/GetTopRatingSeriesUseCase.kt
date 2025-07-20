package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetTopRatingSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getTopRatingSeries(page: Int): List<Series> {
        return seriesRepository.getTopRatingSeries(page)
    }
}