package com.cairosquad.remote.series

import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.RemoteSeriesDataSource
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto

class RemoteSeriesDataSourceImpl(
    private val seriesApiService: SeriesApiService
) : RemoteSeriesDataSource {

    override suspend fun getSeries(seriesId: Long): SeriesDetailsRemoteDto {
        return seriesApiService.getSeries(seriesId)
    }

    override suspend fun getSeriesReviews(seriesId: Long, page: Int): List<ReviewRemoteDto> {
        return seriesApiService.getSeriesReviews(seriesId, page)
    }

    override suspend fun getSimilarSeries(seriesId: Long, page: Int): List<SeriesRemoteDto> {
        return seriesApiService.getSimilarSeries(seriesId, page)
    }

    override suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<ArtistRemoteDto> {
        return seriesApiService.getSeriesTopCast(seriesId, 1)
            .first().cast?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto> {
        return seriesApiService.getSeriesSeasons(seriesId)
            .first().seasons?.filterNotNull().orEmpty()
    }

    override suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<EpisodeRemoteDto> {
        return seriesApiService.getEpisodes(seriesId, seasonNumber)
            .first().episodes ?: emptyList()
    }
}