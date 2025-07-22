package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetAiringTodaySeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getAiringTodaySeries(page: Int,categoryId : String?= null) : List<Series>{
        return seriesRepository.getAiringTodaySeries(page,categoryId)
    }
}