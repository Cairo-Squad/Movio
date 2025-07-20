package com.cairosquad.remote.series

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.utils.callApi
import com.cairosquad.remote.utils.constructUrl
import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.RemoteSeriesDataSource
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteSeriesDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteSeriesDataSource {
    override suspend fun getSeries(seriesId: Long): SeriesDetailsRemoteDto {
        return callApi<SeriesDetailsRemoteDto> {
            httpClient.get(constructUrl("tv/$seriesId")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }
    }

    override suspend fun getSeriesReviews(
        seriesId: Long,
        page: Int
    ): List<ReviewRemoteDto> {
        return callApi<ResultResponse<ReviewRemoteDto>> {
            httpClient.get(constructUrl("tv/$seriesId/reviews")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarSeries(
        seriesId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("tv/$seriesId/similar")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesTopCast(
        seriesId: Long,
        page: Int
    ): List<ArtistRemoteDto> {
        return callApi<CreditResponse> {
            httpClient.get(constructUrl("tv/$seriesId/credits")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.cast?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto> {
        return callApi<SeriesResponse> {
            httpClient.get(constructUrl("tv/$seriesId")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.seasons?.filterNotNull() ?: emptyList()
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int,
    ): List<EpisodeRemoteDto> {
        return callApi<SeasonResponse> {
            httpClient.get(constructUrl("tv/$seriesId/season/$seasonNumber")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.episodes ?: emptyList()
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