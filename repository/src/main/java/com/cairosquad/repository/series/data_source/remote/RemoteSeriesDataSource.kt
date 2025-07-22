package com.cairosquad.repository.series.data_source.remote

import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto

interface RemoteSeriesDataSource {
    suspend fun getSeries(seriesId: Long): SeriesDetailsRemoteDto

    suspend fun getSeriesReviews(seriesId: Long, page: Int): List<ReviewRemoteDto>

    suspend fun getSimilarSeries(seriesId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<ArtistRemoteDto>

    suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto>

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<EpisodeRemoteDto>

    suspend fun getTopRatingSeries(page: Int): List<SeriesRemoteDto>

    suspend fun getMoreRecommendedSeries(page: Int): List<SeriesRemoteDto>

    suspend fun getOnTvSeries(page: Int): List<SeriesRemoteDto>

    suspend fun getAiringTodaySeries(page: Int): List<SeriesRemoteDto>

    suspend fun getTrendingSeries(page: Int): List<SeriesRemoteDto>

    suspend fun getFreeToWatchSeries(page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesByCategory(category: String, page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesGenres(page: Int): List<SeriesRemoteDto>
}