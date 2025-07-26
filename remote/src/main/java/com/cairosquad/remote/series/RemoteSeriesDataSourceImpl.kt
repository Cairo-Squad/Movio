package com.cairosquad.remote.series

import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.RemoteSeriesDataSource
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import java.time.LocalDate

class RemoteSeriesDataSourceImpl(
    private val apiServiceProvider: ApiServiceProvider
) : RemoteSeriesDataSource {
    override suspend fun getSeries(seriesId: Long): SeriesDetailsRemoteDto {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSeries(seriesId) }
    }

    override suspend fun getSeriesReviews(
        seriesId: Long,
        page: Int
    ): List<ReviewRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSeriesReviews(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarSeries(
        seriesId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSimilarSeries(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesTopCast(
        seriesId: Long,
        page: Int
    ): List<ArtistRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSeriesTopCast(seriesId, page) }
            .cast?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSeriesSeasons(seriesId) }
            .seasons?.filterNotNull() ?: emptyList()
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<EpisodeRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getEpisodes(seriesId, seasonNumber) }
            .episodes ?: emptyList()
    }

    override suspend fun getVideoKey(seriesId: Long): String {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getVideoKey(seriesId).getVideoKey() ?: "" }
    }

    override suspend fun getTopRatingSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getTopRatingSeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getMoreRecommendedSeries(page, withGenres = categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getOnTvSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        val today = LocalDate.now()
        val oneWeekAgo = today.minusDays(7)
        return safeCallApi { apiServiceProvider.getSeriesApiService().getOnTvSeries(page,categoryId,
            minDate = oneWeekAgo.toString(), maxDate = today.toString()
            ) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAiringTodaySeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        val today = LocalDate.now()

        val minDate = today.toString()
        val maxDate = today.toString()
        return safeCallApi { apiServiceProvider.getSeriesApiService().getAiringTodaySeries(page,categoryId,
            minDate = minDate,maxDate = maxDate
            ) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        val today = LocalDate.now()
        val thirtyDaysAgo = today.minusDays(30)
        return safeCallApi { apiServiceProvider.getSeriesApiService().getTrendingSeries(page,categoryId,
            minDate = thirtyDaysAgo.toString(),
            maxDate = today.toString()
        ) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getFreeToWatchSeries(page, withGenres = categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByCategory(
        categoryId: String,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSeriesByCategory(categoryId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesGenres(): List<GenreDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getSeriesGenres() }
            .genres?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getPopularSeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllSeries(page: Int,categoryId : String?,sortBy : String?): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSeriesApiService().getAllSeries(page,categoryId,sortBy) }
            .results?.filterNotNull().orEmpty()
    }
}
