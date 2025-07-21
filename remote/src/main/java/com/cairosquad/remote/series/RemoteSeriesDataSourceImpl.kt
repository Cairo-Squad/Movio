package com.cairosquad.remote.series

import com.cairosquad.remote.utils.retrofit.safeCallApi
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

    override suspend fun getTopRatingSeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("tv/top_rated")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedSeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("tv/popular")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getOnTvSeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("tv/on_the_air")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getAiringTodaySeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("tv/airing_today")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingSeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("trending/tv/{day}")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }



    override suspend fun getFreeToWatchSeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("discover/tv")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
                parameter(WITH_WATCH_PROVIDERS,"free")
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByCategory(
        categoryId: String,
        page: Int
    ): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("discover/tv")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
                parameter(WITH_GENRES, categoryId)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getRandomSeries(page: Int): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("discover/tv")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    companion object {
        private const val API_KEY = "api_key"
        private const val PAGE = "page"
        private const val WITH_WATCH_PROVIDERS = "with_watch_providers"
        private const val WITH_GENRES = "with_genres"
    }
}