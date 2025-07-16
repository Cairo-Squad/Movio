package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Series
import com.cairosquad.entity.Season

class GetSeriesDetailsUseCase(
    private val seriesRepository: SeriesRepository
) {

    suspend fun getSeries(seriesId: Long): Series {
        return seriesRepository.getSeries(seriesId)
    }

    suspend fun getSeriesReviews(seriesId: Long): List<Review> {
        return seriesRepository.getSeriesReviews(seriesId)
    }

    suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        return seriesRepository.getSeriesSeasons(seriesId)
    }

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode> {
        return seriesRepository.getEpisodes(seriesId, seasonNumber)
    }

    suspend fun getSimilarSeries(seriesId: Long): List<Series> {
        return seriesRepository.getSimilarSeries(seriesId)
    }

    suspend fun getSeriesTopCast(seriesId: Long): List<Artist> {
        return seriesRepository.getSeriesTopCast(seriesId)
    }
}