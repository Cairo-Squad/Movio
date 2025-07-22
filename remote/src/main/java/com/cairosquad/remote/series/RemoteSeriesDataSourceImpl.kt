package com.cairosquad.remote.series

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
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
        return safeCallApi { seriesApiService.getSeries(seriesId) }
    }

    override suspend fun getSeriesReviews(
        seriesId: Long,
        page: Int
    ): List<ReviewRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesReviews(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarSeries(
        seriesId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getSimilarSeries(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesTopCast(
        seriesId: Long,
        page: Int
    ): List<ArtistRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesTopCast(seriesId, page) }
            .cast?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesSeasons(seriesId) }
            .seasons?.filterNotNull() ?: emptyList()
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<EpisodeRemoteDto> {
        return safeCallApi { seriesApiService.getEpisodes(seriesId, seasonNumber) }
            .episodes ?: emptyList()
    }

    override suspend fun getTopRatingSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getTopRatingSeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getMoreRecommendedSeries(page, withGenres = categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getOnTvSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getOnTvSeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAiringTodaySeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getAiringTodaySeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getTrendingSeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getFreeToWatchSeries(page, withGenres = categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByCategory(
        categoryId: String,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesByCategory(categoryId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesGenres(): List<GenreDto> {
        return safeCallApi { seriesApiService.getSeriesGenres() }
            .genres?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularSeries(page: Int,categoryId : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getPopularSeries(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllSeries(page: Int,categoryId : String?,sortBy : String?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getAllSeries(page,categoryId,sortBy) }
            .results?.filterNotNull().orEmpty()
    }
}
