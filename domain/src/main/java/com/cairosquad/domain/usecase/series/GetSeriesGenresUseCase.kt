package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Series

class GetSeriesGenresUseCase(private val seriesRepository: SeriesRepository) {

    suspend fun getSeriesGenres() : List<Genre>{
        return seriesRepository.getSeriesGenres()
    }
}