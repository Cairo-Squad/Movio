package com.cairosquad.repository.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.series.data_source.remote.RemoteSeriesDataSource

class SeriesRepositoryImpl(
    private val remoteSeriesDataSource: RemoteSeriesDataSource
) : SeriesRepository {
    override suspend fun getSeries(seriesId: Long): Series {
        return remoteSeriesDataSource.getSeries(seriesId).toEntity()
    }

    override suspend fun getSeriesReviews(seriesId: Long, page: Int): List<Review> {
        return remoteSeriesDataSource.getSeriesReviews(seriesId, page).map { it.toEntity() }
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        return remoteSeriesDataSource.getSeriesSeasons(seriesId).map { it.toEntity(seriesId) }
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return remoteSeriesDataSource.getEpisodes(seriesId, seasonNumber).map { it.toEntity() }
    }

    override suspend fun getSimilarSeries(seriesId: Long, page: Int): List<Series> {
        return remoteSeriesDataSource.getSimilarSeries(seriesId, page).map { it.toEntity() }
    }

    override suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<Artist> {
        return remoteSeriesDataSource.getSeriesTopCast(seriesId, page).map { it.toEntity() }
    }
}