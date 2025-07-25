package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Series

class GetAllSeriesUseCase(
    private val seriesRepository: SeriesRepository
) {
    suspend fun getAllSeries(page: Int,categoryId : String?= null,sortType : SortType? = null) : List<Series>{
        return seriesRepository.getAllSeries(page,categoryId,sortType)
    }
}