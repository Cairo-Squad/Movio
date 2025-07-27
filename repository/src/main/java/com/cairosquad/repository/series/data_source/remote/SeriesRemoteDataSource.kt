package com.cairosquad.repository.series.data_source.remote

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto

interface SeriesRemoteDataSource {

    suspend fun getSeriesById(seriesId: Long): SeriesDetailsRemoteDto

    suspend fun getSeriesReviews(seriesId: Long, page: Int): List<ReviewRemoteDto>

    suspend fun getSimilarSeries(seriesId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<ArtistRemoteDto>

    suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto>

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<EpisodeRemoteDto>

    suspend fun getVideoKey(seriesId: Long): String

    suspend fun getTopRatingSeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getMoreRecommendedSeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getOnTvSeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getAiringTodaySeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getTrendingSeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getFreeToWatchSeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getSeriesByCategory(genreId: String, page: Int): List<SeriesRemoteDto>

    suspend fun getSeriesGenres(): List<GenreDto>

    suspend fun getPopularSeries(page: Int,categoryId : String?): List<SeriesRemoteDto>

    suspend fun getAllSeries(page: Int,categoryId : String?,sortBy: String?): List<SeriesRemoteDto>

    suspend fun getSeriesByQuery(query: String, page: Int): List<SeriesRemoteDto>
}