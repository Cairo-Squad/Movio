package com.cairosquad.repository.series.data_source.remote

import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto

interface SeriesRemoteDataSource {

    suspend fun getSeriesById(id: Long): SeriesDetailsRemoteDto

    suspend fun getSeriesReviews(seriesId: Long, page: Int): List<ReviewRemoteDto>

    suspend fun getSimilarSeries(seriesId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto>

    suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto>

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<EpisodeRemoteDto>

    suspend fun getVideoKey(seriesId: Long): String

    suspend fun getTopRatingSeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getMoreRecommendedSeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getOnTvSeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getAiringTodaySeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getTrendingSeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getFreeToWatchSeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getSeriesByCategory(genreId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesGenres(): List<GenreDto>

    suspend fun getPopularSeries(page: Int, genreId: Long?): List<SeriesRemoteDto>

    suspend fun getAllSeries(page: Int, genreId: Long?, sortBy: String?): List<SeriesRemoteDto>

    suspend fun getSeriesByQuery(query: String, page: Int): List<SeriesRemoteDto>
}