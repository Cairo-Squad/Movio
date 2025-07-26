package com.cairosquad.repository.series

import android.util.Log
import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.series.data_source.local.SeriesLocalDataSource
import com.cairosquad.repository.series.data_source.local.toCacheDtoList
import com.cairosquad.repository.series.data_source.local.toEntityList
import com.cairosquad.repository.series.data_source.local.toRequestWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.mappers.tryToCall
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfAiringTodaySeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfAllSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfFreeToWatchSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfMoreRecommendedSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfOnTvSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfPopularSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSearchedSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSeriesByCategory
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSeriesReviews
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSimilarSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfTopRatedSeries
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfTrendingSeries
import com.cairosquad.repository.utils.sharedDto.local.toEntityList
import com.cairosquad.repository.utils.sharedDto.local.toRequestWithReviewsCacheDto
import java.util.Date

class SeriesRepositoryImpl(
    private val seriesRemoteDataSource: SeriesRemoteDataSource,
    private val seriesLocalDataSource: SeriesLocalDataSource
) : SeriesRepository {

    override suspend fun getSimilarSeries(seriesId: Long, page: Int): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getSimilarSeries(seriesId, page) },
            requestCache = getRequestOfSimilarSeries(seriesId, page)
        )
    }

    override suspend fun getTopRatingSeries(page: Int, genreId: String?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getTopRatingSeries(page,genreId) },
            requestCache = getRequestOfTopRatedSeries(page, genreId)
        )
    }

    override suspend fun getTrendingSeries(page: Int, categoryId: String?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getTrendingSeries(page, categoryId) },
            requestCache = getRequestOfTrendingSeries(page, categoryId)
        )
    }

    override suspend fun getMoreRecommendedSeries(page: Int, categoryId: String?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getMoreRecommendedSeries(page, categoryId) },
            requestCache = getRequestOfMoreRecommendedSeries(page, categoryId)
        )
    }

    override suspend fun getOnTvSeries(
        page: Int,
        categoryId: String?
    ): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getOnTvSeries(page,categoryId) },
            requestCache = getRequestOfOnTvSeries(page, categoryId)
        )
    }

    override suspend fun getAiringTodaySeries(
        page: Int,
        categoryId: String?
    ): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getAiringTodaySeries(page,categoryId) },
            requestCache = getRequestOfAiringTodaySeries(page, categoryId)
        )
    }

    override suspend fun getFreeToWatchSeries(page: Int, categoryId: String?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getFreeToWatchSeries(page, categoryId) },
            requestCache = getRequestOfFreeToWatchSeries(page, categoryId)
        )
    }

    override suspend fun getSeriesByCategory(genreId: String, page: Int): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getSeriesByCategory(genreId, page) },
            requestCache = getRequestOfSeriesByCategory(page, genreId)
        )
    }

    override suspend fun getPopularSeries(page: Int, categoryId: String?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getPopularSeries(page, categoryId) },
            requestCache = getRequestOfPopularSeries(page, categoryId)
        )
    }

    override suspend fun getAllSeries(page: Int, categoryId: String?, sortType: SortType?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getAllSeries(page, categoryId, sortType?.sortBy) },
            requestCache = getRequestOfAllSeries(page, categoryId, sortType)
        )
    }

    override suspend fun getSeriesByQuery(query: String, page: Int): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getSeriesByQuery(query, page) },
            requestCache = getRequestOfSearchedSeries(query, page)
        )
    }

    private suspend fun getSeries(
        remoteFetcher: suspend () -> List<SeriesRemoteDto>,
        requestCache: String
    ):List<Series> {
        seriesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return seriesLocalDataSource
            .getSeriesByRequest(request = requestCache)
            .toEntityList()
            .takeIf { it.isNotEmpty() } ?. also { Log.d("asdasd", "getSeries: cache $requestCache ${it.size} ") }
            ?: tryToCall {
                val genres = seriesRemoteDataSource.getSeriesGenres().map { it.toEntity() }
                remoteFetcher()
                    .map { it.toEntity(genres) } . also { Log.d("asdasd", "getSeries: api $requestCache ${it.size} ") }
                    .also { series ->
                        seriesLocalDataSource.insertRequestWithSeries(
                            series.toRequestWithSeriesCacheDto(
                                request = requestCache
                            )
                        )
                    }
            }
    }

    override suspend fun getSeriesById(seriesId: Long): Series {
        seriesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return seriesLocalDataSource
            .getSeriesByRequest(request = getRequestOfSeries(seriesId))
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                seriesRemoteDataSource.getSeriesById(seriesId).toEntity(
                    seriesRemoteDataSource.getVideoKey(seriesId)
                )
            }.also { series ->
                seriesLocalDataSource.insertRequestWithSeries(
                    listOf(series).toRequestWithSeriesCacheDto(request = "series/${seriesId}")
                )
            }
    }
    override suspend fun getSeriesReviews(seriesId: Long, page: Int): List<Review> {
        return seriesLocalDataSource
            .getSeriesReviewsByRequest(getRequestOfSeriesReviews(page, seriesId))
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?:tryToCall {
                seriesRemoteDataSource.getSeriesReviews(seriesId, page).map { it.toEntity() }
            }.also {
                seriesLocalDataSource.insertRequestWithReviews(
                    it.toRequestWithReviewsCacheDto(
                        getRequestOfSeriesReviews(page, seriesId)
                    )
                )
            }
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        return seriesRemoteDataSource.getSeriesSeasons(seriesId).map { it.toEntity(seriesId) }
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return seriesRemoteDataSource.getEpisodes(seriesId, seasonNumber).map { it.toEntity() }
    }

    override suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<Artist> {
        return tryToCall {
            seriesRemoteDataSource.getSeriesTopCast(seriesId, page).map { it.toEntity() }
        }
    }

    override suspend fun getSeriesGenres(): List<Genre> {
        return seriesLocalDataSource
            .getSeriesGenres()
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                seriesRemoteDataSource.getSeriesGenres()
                    .map { it.toEntity() }
                    .also {
                        seriesLocalDataSource.insertSeriesGenres(it.toCacheDtoList())
                    }
            }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}