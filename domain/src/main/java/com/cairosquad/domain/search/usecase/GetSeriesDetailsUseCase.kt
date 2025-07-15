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

    suspend fun getSeriesById(seriesId: Long): Series {
        return seriesDetailsRepository.getSeriesById(seriesId)
    }

    suspend fun getReviewsBySeriesId(seriesId: Long): List<Review> {
        return seriesDetailsRepository.getReviewsBySeriesId(seriesId)
    }

    suspend fun getSeriesSeasonsBySeriesId(seriesId: Long): List<Season> {
        return seriesDetailsRepository.getSeriesSeasonsBySeriesId(seriesId)
    }

    suspend fun getEpisodesBySeriesIdAndSeasonNumber(seriesId: Long, seasonNumber: Int): List<Episode> {
        return seriesDetailsRepository.getEpisodesBySeriesIdAndSeasonNumber(seriesId, seasonNumber)
    }

    suspend fun getSimilarSeries(seriesId: Long): List<Series> {
        return seriesDetailsRepository.getSimilarSeries(seriesId)
    }

    suspend fun getTopCastBySeriesId(seriesId: Long): List<Artist> {
        return seriesDetailsRepository.getTopCastBySeriesId(seriesId)
    }
}