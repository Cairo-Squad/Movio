package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetSeriesByCategoryUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getSeriesByCategory(
        categoryId: String,
        page: Int,
    ): List<Series> {
        return seriesRepository.getSeriesByCategory(categoryId, page)
    }
}