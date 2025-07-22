package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetPopularSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getPopularSeries(page: Int,categoryId : String?= null) : List<Series>{
        return seriesRepository.getPopularSeries(page,categoryId)
    }
}