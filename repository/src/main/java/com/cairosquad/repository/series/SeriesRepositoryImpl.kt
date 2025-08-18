package com.cairosquad.repository.series

import com.cairosquad.domain.model.RatingResult
import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.LanguageRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.repository.series.data_source.local.SeasonEpisodeLocalDataSource
import com.cairosquad.repository.series.data_source.local.SeriesLocalDataSource
import com.cairosquad.repository.series.data_source.local.toCacheCodeWithEpisodesCacheDto
import com.cairosquad.repository.series.data_source.local.toCacheCodeWithSeasonsCacheDto
import com.cairosquad.repository.series.data_source.local.toCacheCodeWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.toCacheDtoList
import com.cairosquad.repository.series.data_source.local.toEntityList
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.toEntity
import com.cairosquad.repository.utils.mappers.tryToCall
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfAiringTodaySeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfAllSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfEpisodes
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfFreeToWatchSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoreRecommendedSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfOnTvSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfPopularSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesByCategory
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesOfArtist
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesReviews
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesSeasons
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSimilarSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTopRatedSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTrendingSeries
import com.cairosquad.repository.utils.sharedDto.local.toCacheCodeWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.toEntityList
import java.util.Date
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val seriesRemoteDataSource: SeriesRemoteDataSource,
    private val seriesLocalDataSource: SeriesLocalDataSource,
    private val seasonEpisodeLocalDataSource: SeasonEpisodeLocalDataSource,
    private val languageRepository: LanguageRepository
) : SeriesRepository {

    override suspend fun getSimilarSeries(seriesId: Long, page: Int): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getSimilarSeries(seriesId, page) },
            cacheCode = getCacheCodeOfSimilarSeries(
                seriesId,
                page,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getTopRatingSeries(page: Int, genreId: Long?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getTopRatingSeries(page, genreId) },
            cacheCode = getCacheCodeOfTopRatedSeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getTrendingSeries(page: Int, genreId: Long?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getTrendingSeries(page, genreId) },
            cacheCode = getCacheCodeOfTrendingSeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getMoreRecommendedSeries(page: Int, genreId: Long?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getMoreRecommendedSeries(page, genreId) },
            cacheCode = getCacheCodeOfMoreRecommendedSeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getOnTvSeries(
        page: Int,
        genreId: Long?
    ): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getOnTvSeries(page, genreId) },
            cacheCode = getCacheCodeOfOnTvSeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getAiringTodaySeries(
        page: Int,
        genreId: Long?
    ): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getAiringTodaySeries(page, genreId) },
            cacheCode = getCacheCodeOfAiringTodaySeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getFreeToWatchSeries(page: Int, genreId: Long?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getFreeToWatchSeries(page, genreId) },
            cacheCode = getCacheCodeOfFreeToWatchSeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getSeriesByCategory(genreId: Long, page: Int): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getSeriesByCategory(genreId, page) },
            cacheCode = getCacheCodeOfSeriesByCategory(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getPopularSeries(page: Int, genreId: Long?): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getPopularSeries(page, genreId) },
            cacheCode = getCacheCodeOfPopularSeries(
                page, genreId,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getAllSeries(
        page: Int,
        genreId: Long?,
        sortType: SortType?
    ): List<Series> {
        return getSeries(
            remoteFetcher = {
                seriesRemoteDataSource.getAllSeries(
                    page,
                    genreId,
                    sortType?.sortBy
                )
            },
            cacheCode = getCacheCodeOfAllSeries(
                page, genreId, sortType,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getSeriesByQuery(query: String, page: Int): List<Series> {
        return tryToCall {
            val genres = seriesRemoteDataSource.getSeriesGenres().map { it.toEntity() }
            seriesRemoteDataSource.getSeriesByQuery(query, page)
                .map { it.toEntity(genres) }
        }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return getSeries(
            remoteFetcher = { seriesRemoteDataSource.getSeriesOfArtist(artistId) },
            cacheCode = getCacheCodeOfSeriesOfArtist(
                artistId,
                languageRepository.getLanguage()
            )
        )
    }

    private suspend fun getSeries(
        remoteFetcher: suspend () -> List<SeriesRemoteDto>,
        cacheCode: String
    ): List<Series> {
        seriesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return seriesLocalDataSource
            .getSeriesByCacheCode(cacheCode = cacheCode)
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                val genres = seriesRemoteDataSource.getSeriesGenres().map { it.toEntity() }
                remoteFetcher()
                    .map { it.toEntity(genres) }
                    .also { series ->
                        seriesLocalDataSource.insertCacheCodeWithSeries(
                            series.toCacheCodeWithSeriesCacheDto(
                                request = cacheCode,
                                languageRepository.getLanguage()

                            )
                        )
                    }
            }
    }

    override suspend fun getSeriesById(id: Long): Series {
        seriesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return seriesLocalDataSource
            .getSeriesByCacheCode(
                cacheCode = getCacheCodeOfSeries(
                    id,
                    languageRepository.getLanguage()
                )
            )
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                seriesRemoteDataSource.getSeriesById(id).toEntity(
                    seriesRemoteDataSource.getVideoKey(id)
                )
            }.also { series ->
                seriesLocalDataSource.insertCacheCodeWithSeries(
                    listOf(series).toCacheCodeWithSeriesCacheDto(
                        request = "series/${id}",
                        languageRepository.getLanguage()
                    )
                )
            }
    }

    override suspend fun getSeriesReviews(seriesId: Long, page: Int): List<Review> {
        return seriesLocalDataSource
            .getSeriesReviewsByCacheCode(
                getCacheCodeOfSeriesReviews(
                    page, seriesId,
                    languageRepository.getLanguage()
                )
            )
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                seriesRemoteDataSource.getSeriesReviews(seriesId, page).map { it.toEntity() }
            }.also {
                seriesLocalDataSource.insertCacheCodeWithReviews(
                    it.toCacheCodeWithReviewsCacheDto(
                        getCacheCodeOfSeriesReviews(
                            page, seriesId,
                            languageRepository.getLanguage()
                        ),
                        languageRepository.getLanguage()

                    )
                )
            }
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        seasonEpisodeLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return seasonEpisodeLocalDataSource
            .getSeasonsByCacheCode(
                cacheCode = getCacheCodeOfSeriesSeasons(
                    seriesId,
                    languageRepository.getLanguage()
                )
            )
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                seriesRemoteDataSource.getSeriesSeasons(seriesId).map { it.toEntity(seriesId) }
                    .also { seasons ->
                        seasonEpisodeLocalDataSource.insertCacheCodeWithSeasons(
                            seasons.toCacheCodeWithSeasonsCacheDto(
                                request = getCacheCodeOfSeriesSeasons(
                                    seriesId,
                                    languageRepository.getLanguage()
                                ),
                                languageRepository.getLanguage()

                            )
                        )
                    }
            }
    }

    override suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode> {
        seasonEpisodeLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return seasonEpisodeLocalDataSource
            .getEpisodesByCacheCode(
                cacheCode = getCacheCodeOfEpisodes(
                    seriesId, seasonNumber,
                    languageRepository.getLanguage()
                )
            )
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                seriesRemoteDataSource.getEpisodes(seriesId, seasonNumber).map { it.toEntity() }
                    .also { episodes ->
                        seasonEpisodeLocalDataSource.insertCacheCodeWithEpisodes(
                            episodes.toCacheCodeWithEpisodesCacheDto(
                                request = getCacheCodeOfEpisodes(
                                    seriesId, seasonNumber,
                                    languageRepository.getLanguage()
                                ),
                                languageRepository.getLanguage()

                            )
                        )
                    }
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
                        seriesLocalDataSource.insertSeriesGenres(
                            it.toCacheDtoList(
                                languageRepository.getLanguage()
                            )
                        )
                    }
            }
    }

    override suspend fun addSeriesRating(seriesId: Long, rating: Float): RatingResult {
        return tryToCall {
            val response = seriesRemoteDataSource.addSeriesRating(seriesId, rating)
            RatingResult(
                statusCode = response.statusCode,
                statusMessage = response.statusMessage
            )
        }
    }

    override suspend fun deleteSeriesRating(seriesId: Long) {
        tryToCall {
            seriesRemoteDataSource.deleteSeriesRating(seriesId)
        }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 86_400_000
    }
}