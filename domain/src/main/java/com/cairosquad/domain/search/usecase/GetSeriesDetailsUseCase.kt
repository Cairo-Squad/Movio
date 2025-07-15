package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SeriesDetailsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Series
import com.cairosquad.entity.Season

class GetSeriesDetailsUseCase(
    private val seriesDetailsRepository: SeriesDetailsRepository
) {

    suspend fun getSeries(seriesId: Long): Series {
        return seriesDetailsRepository.getSeries(seriesId)
    }

    suspend fun getSeriesReviews(seriesId: Long): List<Review> {
        return seriesDetailsRepository.getSeriesReviews(seriesId)
    }

    suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        return seriesDetailsRepository.getSeriesSeasons(seriesId)
    }

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode> {
        return seriesDetailsRepository.getEpisodes(seriesId, seasonNumber)
    }

    suspend fun getSimilarSeries(seriesId: Long): List<Series> {
        return seriesDetailsRepository.getSimilarSeries(seriesId)
    }

    suspend fun getSeriesTopCast(seriesId: Long): List<Artist> {
        return seriesDetailsRepository.getSeriesTopCast(seriesId)
    }
}